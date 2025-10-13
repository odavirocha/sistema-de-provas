package dev.odroca.api_provas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class MultipleCorrectOptionsException extends RuntimeException {
    
    public MultipleCorrectOptionsException() {
        super("Só pode haver uma resposta correta.");
    }

}
