package dev.odroca.api_provas.exception;

import java.util.UUID;

public class OptionNotFoundExcetion extends RuntimeException {
    
    public OptionNotFoundExcetion(UUID optionId) {
        super("Opção não encontrada com ID: " + optionId);
    }

}
