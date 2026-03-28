package dev.odroca.api_provas.dto.test;

import java.util.List;

import dev.odroca.api_provas.dto.question.QuestionAnswerModelDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public record AnswerTestRequestDTO(
    @NotNull(message = "O campo 'questions' não pode ser vazio!")
    List<QuestionAnswerModelDTO> questions
) {}
