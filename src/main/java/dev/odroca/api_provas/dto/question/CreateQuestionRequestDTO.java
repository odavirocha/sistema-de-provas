package dev.odroca.api_provas.dto.question;

import jakarta.validation.constraints.NotNull;

public record CreateQuestionRequestDTO(
    @NotNull(message = "O campo 'question' não pode ser nulo!")
    CreateQuestionModelDTO question
) {}
