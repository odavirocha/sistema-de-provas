package dev.odroca.api_provas.service;

import java.time.Instant;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import dev.odroca.api_provas.dto.login.LoginRequestDTO;
import dev.odroca.api_provas.dto.login.LoginResponseDTO;
import dev.odroca.api_provas.entity.UserEntity;
import dev.odroca.api_provas.exception.InvalidCredentialsException;
import dev.odroca.api_provas.repository.UserRepository;

public class AuthService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO loginInformations) {

        UserEntity user = userRepository.findByEmail(loginInformations.email()).orElseThrow(() -> new InvalidCredentialsException());

        if (!passwordEncoder.matches(loginInformations.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        Instant timeNow = Instant.now();

        var claims = JwtClaimsSet.builder()
            .issuer("https://localhost:2709/login")
            .subject(user.getId().toString())
            .issuedAt(timeNow)
            .expiresAt(timeNow.plusSeconds(60 * 5)) // 5 minutos
            .build();
        
        var jwtToken = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
            
        return new LoginResponseDTO();
    }
    
}
