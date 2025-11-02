package dev.odroca.api_provas.exception;

import java.util.UUID;

public class QuestionAnswerValidationException extends RuntimeException {
    
    public QuestionAnswerValidationException(UUID questionId) {
        super("Não foi possivel encontrar nenhuma resposta correta para a questão: " + questionId);
    }
    
}
