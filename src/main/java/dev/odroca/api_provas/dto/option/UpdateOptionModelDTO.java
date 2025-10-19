package dev.odroca.api_provas.dto.option;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOptionModelDTO {
    
    private UUID optionId;
    
    @NotBlank(message = "O campo 'value' não pode ser vazio.")
    private String value;
    
    @NotNull(message = "O campo 'isCorrect' não pode ser nulo.")
    private Boolean isCorrect;

}
