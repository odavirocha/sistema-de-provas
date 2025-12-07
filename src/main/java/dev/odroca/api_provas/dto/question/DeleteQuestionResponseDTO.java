package dev.odroca.api_provas.dto.question;

import java.util.UUID;

public record DeleteQuestionResponseDTO(
    UUID id,
    String message
) {}
