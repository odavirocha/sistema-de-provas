package dev.odroca.api_provas.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class QuestionRequestDTO {

    @NotBlank(message = "O nome não pode estar em branco!")
    @NotEmpty(message = "O nome não pode estar vazio!")
    @NotNull(message = "O nome não pode ser nulo!")
    UUID testId;

    @NotNull(message = "O nome não pode ser nulo!")
    QuestionModelDTO question;

}
