package dev.odroca.api_provas.dto.question;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateQuestionResponseDTO {

    UUID id;
    String question;
    int totalOptions;
    UUID correctOptionId;
    String message;
    
}
