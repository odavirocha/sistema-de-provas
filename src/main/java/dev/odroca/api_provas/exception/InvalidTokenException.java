package dev.odroca.api_provas.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("O token é inválido!");
    }
}
