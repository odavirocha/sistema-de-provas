package dev.odroca.api_provas.dto.question;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateQuestionResponseDTO {

    private UUID questionId;
    private String message;
    
}
