package dev.odroca.api_provas.dto.test;

import java.util.List;

import dev.odroca.api_provas.dto.question.QuestionResultModelDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AnswerTestResponseDTO {

    int score;
    List<QuestionResultModelDTO> questions;
    String message;
}
