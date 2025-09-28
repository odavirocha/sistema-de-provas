package dev.odroca.api_provas.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OptionNotFoundExcetion extends RuntimeException {
    
    public OptionNotFoundExcetion(UUID optionId) {
        super("Opção não encontrada com ID: " + optionId);
    }

}
