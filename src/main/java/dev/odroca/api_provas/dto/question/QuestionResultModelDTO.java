package dev.odroca.api_provas.dto.question;

import java.util.UUID;

public record QuestionResultModelDTO (
    UUID questionId,
    UUID selectedOptionId,
    UUID correctOptionId,
    Boolean isCorrect
){}
