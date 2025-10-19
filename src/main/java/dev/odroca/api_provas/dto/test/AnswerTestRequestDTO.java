package dev.odroca.api_provas.dto.test;

import java.util.List;

import dev.odroca.api_provas.dto.question.QuestionAnswerModelDTO;
import lombok.Getter;

@Getter
public class AnswerTestRequestDTO {
    
    private List<QuestionAnswerModelDTO> questions;
    
}
