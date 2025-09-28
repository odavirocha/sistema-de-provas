package dev.odroca.api_provas.dto.question;

import java.util.UUID;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateQuestionResponseDTO {

    UUID questionId;
    String message;
    
}
