package dev.odroca.api_provas.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
// @Data
public class UpdateQuestionResponseDTO {

    UUID questionId;
    String message;
    
}
