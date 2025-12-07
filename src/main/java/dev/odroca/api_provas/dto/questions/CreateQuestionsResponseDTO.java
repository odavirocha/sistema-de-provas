package dev.odroca.api_provas.dto.questions;

import java.util.UUID;

public record CreateQuestionsResponseDTO(
    UUID id,
    int totalCreatedQuestions,
    String message
) {}
