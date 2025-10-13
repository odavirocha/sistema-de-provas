package dev.odroca.api_provas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MultipleCorrectOptionsException extends RuntimeException {
    
    public MultipleCorrectOptionsException() {
        super("Só pode haver uma resposta correta.");
    }

}
