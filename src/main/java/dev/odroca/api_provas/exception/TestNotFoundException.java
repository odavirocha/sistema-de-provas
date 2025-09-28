package dev.odroca.api_provas.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TestNotFoundException extends RuntimeException{
    
    public TestNotFoundException(UUID testId) {
        super("Prova não encontrada com ID: " + testId);
    }
    
}
