package dev.odroca.api_provas.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.odroca.api_provas.entity.RefreshTokenEntity;
import dev.odroca.api_provas.entity.UserEntity;
import dev.odroca.api_provas.exception.InvalidTokenException;
import dev.odroca.api_provas.exception.UnauthorizedException;
import dev.odroca.api_provas.repository.RefreshTokenRepository;
import dev.odroca.api_provas.utils.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
@Transactional
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshRepository;
    
    @Autowired
    private JwtEncoder jwt;
    
    @Autowired
    private CookieUtil cookie;

    @Transactional
    public void refresh(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        String refreshTokenCookie = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshTokenCookie = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshTokenCookie == null) throw new UnauthorizedException();

        RefreshTokenEntity refreshTokenEntity = refreshRepository.findByRefreshToken(UUID.fromString(refreshTokenCookie)).orElseThrow(() -> new UnauthorizedException());

        Instant timeNow = Instant.now();

        if (refreshTokenEntity.getExpiryDate().isBefore(timeNow)) throw new InvalidTokenException();

        UserEntity user = refreshTokenEntity.getUser();

        deleteRefreshToken(refreshTokenEntity.getRefreshToken());

        int accessTokenExpireIn = 60 * 5; // 5 minutos
        int refreshTokenExpireIn = 60 * 60 * 24 * 7; // 7 dias

        var claims = JwtClaimsSet.builder()
            .issuer("https://localhost:8080/auth/login")
            .subject(user.getId().toString())
            .issuedAt(timeNow)
            .expiresAt(timeNow.plusSeconds(accessTokenExpireIn))
            .claim("role", user.getRole())
            .build();

        String accessToken = jwt.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        String refreshToken = createRefreshToken(user, timeNow.plusSeconds(refreshTokenExpireIn)).getRefreshToken().toString();

        cookie.addCookie(response, "accessToken", accessToken, accessTokenExpireIn);
        cookie.addCookie(response, "refreshToken", refreshToken, refreshTokenExpireIn);
    }
    
    public Optional<RefreshTokenEntity> verifyExistRefreshTokenOfUser(UUID userId) {
        return refreshRepository.findByUserId(userId);
    }

    @Transactional
    public RefreshTokenEntity createRefreshToken(UserEntity user, Instant expiryDate) {

        Instant timeNow = Instant.now();
            
        RefreshTokenEntity tokenEntity = new RefreshTokenEntity();
    
        tokenEntity.setUser(user);
        tokenEntity.setExpiryDate(expiryDate);
        tokenEntity.setIssuedAt(timeNow);

        return refreshRepository.save(tokenEntity);
    }

    @Transactional
    public void deleteRefreshToken(UUID token) {
        refreshRepository.deleteById(token);
        refreshRepository.flush();
    }
    
}
