package dev.odroca.api_provas.dto.question;

import java.util.UUID;

import lombok.Getter;

@Getter
public class QuestionResultModelDTO {
    
    private UUID questionId;
    private UUID selectedOptionId;
    private UUID correctOptionId;
    private Boolean isCorrect;

}
