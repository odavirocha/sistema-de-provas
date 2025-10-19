package dev.odroca.api_provas.dto.question;

import java.util.UUID;

import lombok.Getter;

@Getter
public class QuestionAnswerModelDTO {
    
    private UUID questionId;
    private UUID isCorrectId;
    
}
