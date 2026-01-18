package dev.odroca.api_provas.dto.test;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TestResponseDTO {
    
    private UUID testId;
    private String name;
    private int totalQuestions;
    
}
