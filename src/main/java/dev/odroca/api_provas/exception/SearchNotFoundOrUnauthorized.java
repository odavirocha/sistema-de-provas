package dev.odroca.api_provas.exception;

public class SearchNotFoundOrUnauthorized extends RuntimeException {
    public SearchNotFoundOrUnauthorized() {
        super("Busca não encontrada ou você não tem autorização.");
    }
}
