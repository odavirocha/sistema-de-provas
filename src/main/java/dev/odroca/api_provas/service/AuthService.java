package dev.odroca.api_provas.service;

import java.time.Instant;

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
import dev.odroca.api_provas.entity.UserEntity;
import dev.odroca.api_provas.enums.Role;
import dev.odroca.api_provas.exception.EmailAlreadyExistsException;
import dev.odroca.api_provas.exception.InvalidCredentialsException;
import dev.odroca.api_provas.repository.UserRepository;

@Service
@Transactional
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private BCryptPasswordEncoder bcrypt;

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

    public void login(LoginRequestDTO loginInformations) {

        UserEntity user = userRepository.findByEmail(loginInformations.email()).orElseThrow(() -> new InvalidCredentialsException());

        if (!bcrypt.matches(loginInformations.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        Instant timeNow = Instant.now();
        long expireIn = 60 * 5; // 5 minutos

        var claims = JwtClaimsSet.builder()
            .issuer("https://localhost:2709/login")
            .subject(user.getId().toString())
            .issuedAt(timeNow)
            .expiresAt(timeNow.plusSeconds(expireIn))
            .claim("roles", user.getRoles())
            .build();
        
        String jwtToken = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    
}
