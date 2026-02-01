package dev.odroca.api_provas.dto.option;

import java.util.UUID;

public record GetOptionModelDTO(
    UUID id,
    String value,
    Boolean isCorrect
) {}
