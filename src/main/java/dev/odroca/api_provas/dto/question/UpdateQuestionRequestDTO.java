package dev.odroca.api_provas.dto.question;

import java.util.List;

import dev.odroca.api_provas.dto.option.UpdateOptionModelDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record UpdateQuestionRequestDTO(
    @NotBlank(message = "O campo 'question' não pode estar em branco.")
    String question,
    @NotEmpty(message = "O campo 'options' não pode ser vazio.")
    List<@Valid UpdateOptionModelDTO> options
) {}
