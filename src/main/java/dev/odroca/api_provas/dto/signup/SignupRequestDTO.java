package dev.odroca.api_provas.dto.signup;

import dev.odroca.api_provas.validation.annotation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record SignupRequestDTO(
    @Email
    String email,

    @Password
    @Size(max = 72, message = "A senha não deve exceder 72 caracteres.")
    String password
) {}
