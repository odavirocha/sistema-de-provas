package dev.odroca.api_provas.dto;

import jakarta.validation.constraints.NotNull;

public class UpdateQuestionRequestDTO {

    @NotNull(message = "O campo 'question' não pode ser nulo!")
    QuestionModelDTO question;
    
}
