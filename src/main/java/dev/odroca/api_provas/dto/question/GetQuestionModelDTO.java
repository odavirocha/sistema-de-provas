package dev.odroca.api_provas.dto.question;

import java.util.List;
import java.util.UUID;

import dev.odroca.api_provas.dto.option.GetOptionModelDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetQuestionModelDTO {
    
    private UUID id;
    private String question;
    private List<GetOptionModelDTO> options;
    
}
