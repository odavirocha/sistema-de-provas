package dev.odroca.api_provas.dto.test;

import java.util.List;

import dev.odroca.api_provas.dto.question.QuestionResultModelDTO;
import lombok.Getter;

@Getter
public class AnswerTestResponseDTO {

    Long score;
    List<QuestionResultModelDTO> question;
    String message;
}
