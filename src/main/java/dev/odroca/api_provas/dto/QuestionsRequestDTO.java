package dev.odroca.api_provas.dto;

import java.util.List;
import java.util.UUID;

import lombok.Getter;

@Getter
public class QuestionsRequestDTO {

    UUID testId;
    List<QuestionModelDTO> questions;
    
}
