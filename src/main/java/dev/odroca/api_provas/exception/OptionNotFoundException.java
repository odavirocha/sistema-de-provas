package dev.odroca.api_provas.exception;

import java.util.UUID;

public class OptionNotFoundException extends RuntimeException {
    
    public OptionNotFoundException() {
        super("Um ou mais IDs fornecidos não foram encontrado.");
    }

    public OptionNotFoundException(UUID optionId) {
        super("Opção não encontrada com ID: " + optionId);
    }

}
