package dev.odroca.api_provas.dto.question;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateQuestionRequestDTO {

    @NotNull(message = "O campo 'testId' não pode ser nulo!")
    private UUID testId;

    @NotNull(message = "O campo 'question' não pode ser nulo!")
    private CreateQuestionModelDTO question;

}
