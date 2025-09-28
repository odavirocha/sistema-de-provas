package dev.odroca.api_provas.dto.question;

import java.util.List;

import dev.odroca.api_provas.dto.option.CreateOptionModelDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateQuestionModelDTO {

    @NotBlank(message = "O campo 'question' não pode estar em branco.")
    private String question;
    
    @NotEmpty(message = "O campo 'options' não pode ser vazio.")
    private List<@Valid CreateOptionModelDTO> options;
    
}
