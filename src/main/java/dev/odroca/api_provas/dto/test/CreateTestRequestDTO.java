package dev.odroca.api_provas.dto.test;

import jakarta.validation.constraints.NotNull;

public record CreateTestRequestDTO(
    @NotNull
    String name
) {}
