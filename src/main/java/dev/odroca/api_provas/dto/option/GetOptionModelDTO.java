package dev.odroca.api_provas.dto.option;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetOptionModelDTO {

    UUID id;
    String value;
    Boolean isCorrect;
    
}
