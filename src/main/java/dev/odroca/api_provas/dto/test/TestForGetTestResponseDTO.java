package dev.odroca.api_provas.dto.test;

import java.util.List;

import dev.odroca.api_provas.dto.question.GetQuestionModelDTO;

public record TestForGetTestResponseDTO(
    String testName,
    List<GetQuestionModelDTO> questions
) {}
