package dev.odroca.api_provas.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateQuestionsResponseDTO {

    UUID id;
    int totalQuestions;
    String message;
    
}
