package dev.odroca.api_provas.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class QuestionResponseDTO {

    UUID id;
    String question;
    int totalOptions;
    UUID correctOptionId;
    String message;
    
}
