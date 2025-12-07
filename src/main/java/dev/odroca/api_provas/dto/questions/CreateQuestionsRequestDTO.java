package dev.odroca.api_provas.dto.questions;

import java.util.List;
import java.util.UUID;

import dev.odroca.api_provas.dto.question.CreateQuestionModelDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record CreateQuestionsRequestDTO(
    @NotEmpty(message = "O campo 'questions' não pode estar vazia.")
    List<@Valid CreateQuestionModelDTO> questions
) {}
