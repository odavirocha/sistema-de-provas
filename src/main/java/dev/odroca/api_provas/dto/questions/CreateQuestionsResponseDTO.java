package dev.odroca.api_provas.dto.questions;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

public record CreateQuestionsResponseDTO(
UUID id,
int totalCreatedQuestions,
String message
) {}