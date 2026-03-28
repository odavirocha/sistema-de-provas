package dev.odroca.api_provas.dto.question;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public record QuestionAnswerModelDTO (
    @NotNull(message = "O campo 'questionId' não pode ser nulo!")
    UUID questionId,
    @NotNull(message = "O campo 'selectedOptionId' não pode ser nulo!")
    UUID selectedOptionId
){}
