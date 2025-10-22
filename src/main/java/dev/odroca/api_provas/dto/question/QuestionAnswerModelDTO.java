package dev.odroca.api_provas.dto.question;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class QuestionAnswerModelDTO {
    
    @NotNull(message = "O campo 'questionId' não pode ser nulo!")
    private UUID questionId;
    @NotNull(message = "O campo 'selectedOptionId' não pode ser nulo!")
    private UUID selectedOptionId;
    
}
