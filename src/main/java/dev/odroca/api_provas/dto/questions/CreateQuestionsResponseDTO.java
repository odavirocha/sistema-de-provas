package dev.odroca.api_provas.dto.questions;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateQuestionsResponseDTO {

    private UUID id;
    private int totalCreatedQuestions;
    private String message;
    
}
