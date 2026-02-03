package dev.odroca.api_provas.dto.option;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateOptionModelDTO(
    @NotNull(message = "O campo 'optionId' não pode ser vazio.")
    UUID optionId,
    @NotBlank(message = "O campo 'value' não pode ser vazio.")
    String value,
    @NotNull(message = "O campo 'isCorrect' não pode ser nulo.")
    Boolean isCorrect
) {}
