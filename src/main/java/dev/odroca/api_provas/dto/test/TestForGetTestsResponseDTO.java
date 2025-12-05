package dev.odroca.api_provas.dto.test;

import java.util.UUID;

public record TestForGetTestsResponseDTO(
    UUID testId,
    String name,
    int totalQuestions
) {}
