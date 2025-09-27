package dev.odroca.api_provas.dto;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateQuestionsRequestDTO {

    @NotNull(message = "O campo 'testId' não pode ser nulo.")
    UUID testId;

    @NotEmpty(message = "O campo 'questions' não pode estar vazia.")
    List<@Valid CreateQuestionModelDTO> questions;
    
}
