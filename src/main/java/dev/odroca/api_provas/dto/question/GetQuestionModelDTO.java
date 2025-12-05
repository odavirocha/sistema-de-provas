package dev.odroca.api_provas.dto.question;

import java.util.List;
import java.util.UUID;

import dev.odroca.api_provas.dto.option.GetOptionModelDTO;

public record GetQuestionModelDTO (
    UUID id,
    String question,
    List<GetOptionModelDTO> options
){}
