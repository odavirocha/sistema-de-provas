package dev.odroca.api_provas.dto.option;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateOptionModelDTO {

    @NotBlank(message = "O campo 'value' não pode estar em branco.")
    String value;

    @NotNull(message = "O campo 'isCorrect' não pode ser nulo.")
    Boolean isCorrect;

}
