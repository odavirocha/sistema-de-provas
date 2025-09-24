package dev.odroca.api_provas.exception;

import java.util.UUID;

public class QuestionNotFoundException extends RuntimeException{

    public QuestionNotFoundException(UUID questionId) {
        super("Questão não encontrada com ID:" + questionId);
    }

}
