package dev.odroca.api_provas.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("Já existe uma conta com esse email!");
    }
}
