package dev.odroca.api_provas.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class TestResponseDTO {
    
    private UUID testId;
    private String name;
    
}
