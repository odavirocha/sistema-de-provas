package dev.odroca.api_provas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.RestController;

import dev.odroca.api_provas.dto.login.LoginRequestDTO;
import dev.odroca.api_provas.dto.login.LoginResponseDTO;
import dev.odroca.api_provas.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;

    private final JwtEncoder jwtEncoder;

    private BCryptPasswordEncoder passwordEncoder;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginInformations) {
        LoginResponseDTO response = authService.login(loginInformations);
        return new ResponseEntity<LoginResponseDTO>(response, HttpStatus.OK);
    }
    

}
