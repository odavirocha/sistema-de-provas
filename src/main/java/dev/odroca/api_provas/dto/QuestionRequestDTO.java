package dev.odroca.api_provas.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class QuestionRequestDTO {

    @NotNull(message = "O nome não pode ser nulo!")
    UUID testId;

    @NotNull(message = "O nome não pode ser nulo!")
    QuestionModelDTO question;

}
