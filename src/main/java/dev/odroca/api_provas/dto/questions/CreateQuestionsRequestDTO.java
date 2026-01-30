package dev.odroca.api_provas.dto.questions;

import java.util.Set;

import dev.odroca.api_provas.dto.question.CreateQuestionModelDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateQuestionsRequestDTO {

    @NotEmpty(message = "O campo 'questions' não pode estar vazia.")
    private Set<@Valid CreateQuestionModelDTO> questions;
    
}
