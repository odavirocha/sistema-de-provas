package dev.odroca.api_provas.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class QuestionAnswerValidationException extends RuntimeException {
    
    public QuestionAnswerValidationException(UUID questionId) {
        super("Não foi possivel encontrar nenhuma resposta correta para a questão: " + questionId);
    }
    
}
