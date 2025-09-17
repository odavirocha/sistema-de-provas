package dev.odroca.api_provas.dto;

import java.util.List;
import java.util.UUID;

import dev.odroca.api_provas.model.QuestionModel;
import lombok.Data;

@Data
public class TestResponseDTO {
    
    private UUID testId;
    private String name;
    private List<QuestionModel> questions;
    
}
