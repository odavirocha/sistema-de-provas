package dev.odroca.api_provas.dto.question;

import java.util.Set;
import java.util.UUID;

import dev.odroca.api_provas.dto.option.GetOptionModelDTO;

public record GetQuestionModelDTO(
    UUID id,
    String question,
    Set<GetOptionModelDTO> options
) {}
