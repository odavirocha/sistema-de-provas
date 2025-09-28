package dev.odroca.api_provas.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetOptionModelDTO {

    UUID id;
    String value;
    Boolean isCorrect;
    
}
