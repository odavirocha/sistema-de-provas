package dev.odroca.api_provas.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("Você não tem acesso!");
    }
}
