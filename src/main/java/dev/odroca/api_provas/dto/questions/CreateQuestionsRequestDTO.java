package dev.odroca.api_provas.dto.questions;

import java.util.Set;
import java.util.UUID;

import dev.odroca.api_provas.dto.question.CreateQuestionModelDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateQuestionsRequestDTO {

    @NotNull(message = "O campo 'testId' não pode ser nulo.")
    private UUID testId;

    @NotEmpty(message = "O campo 'questions' não pode estar vazia.")
    private Set<@Valid CreateQuestionModelDTO> questions;
    
}
