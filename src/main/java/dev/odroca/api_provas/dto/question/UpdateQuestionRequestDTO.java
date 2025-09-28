package dev.odroca.api_provas.dto.question;

import java.util.List;

import dev.odroca.api_provas.dto.option.UpdateOptionModelDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class UpdateQuestionRequestDTO {

    @NotBlank(message = "O campo 'question' não pode estar em branco.")
    private String question;
    
    @NotEmpty(message = "O campo 'options' não pode ser vazio.")
    private List<@Valid UpdateOptionModelDTO> options;
    
}
