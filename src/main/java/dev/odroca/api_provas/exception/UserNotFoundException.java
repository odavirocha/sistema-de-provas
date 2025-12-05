package dev.odroca.api_provas.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super("Usuário não encontrado!");
    }
}
