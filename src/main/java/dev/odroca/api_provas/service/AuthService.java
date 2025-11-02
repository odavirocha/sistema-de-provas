package dev.odroca.api_provas.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.odroca.api_provas.dto.login.LoginRequestDTO;
import dev.odroca.api_provas.dto.signup.SignupRequestDTO;
import dev.odroca.api_provas.dto.signup.SignupResponseDTO;
import dev.odroca.api_provas.entity.RefreshTokenEntity;
import dev.odroca.api_provas.entity.UserEntity;
import dev.odroca.api_provas.enums.Role;
import dev.odroca.api_provas.exception.EmailAlreadyExistsException;
import dev.odroca.api_provas.exception.InvalidCredentialsException;
import dev.odroca.api_provas.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Service
@Transactional
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenService refreshService;

    @Autowired
    private JwtEncoder jwt;

    @Autowired
    private BCryptPasswordEncoder bcrypt;

    @Transactional
    public SignupResponseDTO signup(SignupRequestDTO signupInformations) {

        Boolean userExists = userRepository.findByEmail(signupInformations.email()).isPresent();

        if (userExists) throw new EmailAlreadyExistsException();

        String passwordHashed = bcrypt.encode(signupInformations.password());

        UserEntity user = new UserEntity();
        user.setEmail(signupInformations.email());
        user.setPassword(passwordHashed);
        user.setRoles(Role.USER);

        userRepository.save(user);

        return new SignupResponseDTO("Conta criada com sucesso!");
    }

    @Transactional
    public void login(LoginRequestDTO loginInformations, HttpServletResponse response) {

        UserEntity user = userRepository.findByEmail(loginInformations.email()).orElseThrow(() -> new InvalidCredentialsException());

        if (!bcrypt.matches(loginInformations.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        Instant timeNow = Instant.now();
        int accessTokenExpireIn = 60 * 5; // 5 minutos
        int refreshTokenExpireIn = 60 * 60 * 24 * 7; // 7 dias

        var claims = JwtClaimsSet.builder()
            .issuer("https://localhost:8080/auth/login")
            .subject(user.getId().toString())
            .issuedAt(timeNow)
            .expiresAt(timeNow.plusSeconds(accessTokenExpireIn))
            .claim("roles", user.getRoles())
            .build();
        
        String accessToken = jwt.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        Optional<RefreshTokenEntity> existingRefreshToken = refreshService.verifyExistRefreshTokenOfUser(user.getId());

        if (existingRefreshToken.isPresent()) {
            refreshService.deleteRefreshToken(existingRefreshToken.get().getRefreshToken());
        }

        RefreshTokenEntity refreshToken = refreshService.createRefreshToken(user, Instant.now().plusSeconds(refreshTokenExpireIn));

        addCookie(response, "accessToken", accessToken, accessTokenExpireIn);
        addCookie(response, "refreshToken", refreshToken.getRefreshToken().toString(), refreshTokenExpireIn); // 7 Dias

    }

    private void addCookie(HttpServletResponse response, String name, String value, int expireIn) {

        Cookie cookie = new Cookie(name, value);

        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(expireIn);
        cookie.setAttribute("SameSite", "Strict");

        response.addCookie(cookie);
        
    }

}
