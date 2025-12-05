package dev.odroca.api_provas.dto.test;

import java.util.List;

import dev.odroca.api_provas.dto.question.QuestionResultModelDTO;

public record AnswerTestResponseDTO(
    List<QuestionResultModelDTO> questions,
    String message,
    int correctCount,
    int incorrectCount
) {}
