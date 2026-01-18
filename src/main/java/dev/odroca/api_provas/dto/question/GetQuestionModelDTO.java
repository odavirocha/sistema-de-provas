package dev.odroca.api_provas.dto.question;

import java.util.Set;
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
    private Set<GetOptionModelDTO> options;
    
}
