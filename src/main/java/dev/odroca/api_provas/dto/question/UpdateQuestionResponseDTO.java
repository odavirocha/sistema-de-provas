package dev.odroca.api_provas.dto.question;

import java.util.UUID;

public record UpdateQuestionResponseDTO(
    UUID questionId,
    String message
) {}
