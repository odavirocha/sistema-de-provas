package dev.odroca.api_provas.exception;

public class TestNullNameException extends IllegalArgumentException {
    
    public TestNullNameException() {
        super("A prova precisa ter um nome!");
    }
    
}
