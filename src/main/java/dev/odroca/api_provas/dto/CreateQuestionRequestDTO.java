package dev.odroca.api_provas.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateQuestionRequestDTO {

    @NotNull(message = "O campo 'testId' não pode ser nulo!")
    UUID testId;

    @NotNull(message = "O campo 'question' não pode ser nulo!")
    CreateQuestionModelDTO question;

}
