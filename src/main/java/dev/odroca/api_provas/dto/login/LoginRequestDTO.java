package dev.odroca.api_provas.dto.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
    @Email(message = "O campo 'email' é obrigatório!")
    String email, 
    @NotBlank(message = "O campo 'password' é obrigatório!")
    String password
    ) {}
