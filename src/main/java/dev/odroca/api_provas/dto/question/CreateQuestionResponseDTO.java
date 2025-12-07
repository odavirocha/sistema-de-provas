package dev.odroca.api_provas.dto.question;

import java.util.UUID;

public record CreateQuestionResponseDTO(
    UUID id,
    String question,
    int totalOptions,
    UUID correctOptionId,
    String message
) {}
