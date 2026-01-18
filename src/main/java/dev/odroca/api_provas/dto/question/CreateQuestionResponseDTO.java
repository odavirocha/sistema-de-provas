package dev.odroca.api_provas.dto.question;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateQuestionResponseDTO {

    private UUID id;
    private String question;
    private int totalOptions;
    private UUID correctOptionId;
    private String message;
    
}
