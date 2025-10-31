package dev.odroca.api_provas.dto.signup;

import dev.odroca.api_provas.validation.annotation.Password;
import jakarta.validation.constraints.Email;

public record SignupRequestDTO(
    @Email
    String email,

    @Password
    String password
) {}
