package dev.odroca.api_provas.dto;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetQuestionModelDTO {
    
    UUID id;
    String question;
    List<GetOptionModelDTO> options;
    
}
