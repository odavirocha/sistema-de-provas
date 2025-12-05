package dev.odroca.api_provas.exception;

public class MultipleCorrectOptionsException extends RuntimeException {
    
    public MultipleCorrectOptionsException() {
        super("Só pode haver uma resposta correta.");
    }

}
