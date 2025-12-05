package dev.odroca.api_provas.model;

import java.util.List;
import java.util.UUID;

import dev.odroca.api_provas.dto.question.CreateQuestionModelDTO;
import lombok.Data;

@Data
public class TestModelDTO {

    private UUID id;
    private String name;
    private List<CreateQuestionModelDTO> questions;
    
}
