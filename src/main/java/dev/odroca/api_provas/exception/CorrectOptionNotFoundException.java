package dev.odroca.api_provas.exception;


public class CorrectOptionNotFoundException extends RuntimeException {

    public CorrectOptionNotFoundException() {
        super("É necessário uma opção correta!");
    }
    
}
