package dev.odroca.api_provas.exception;

import java.util.UUID;

public class TestNotFoundException extends RuntimeException{
    
    public TestNotFoundException(UUID testId) {
        super("Prova não encontrada com ID:" + testId);
    }
    
}
