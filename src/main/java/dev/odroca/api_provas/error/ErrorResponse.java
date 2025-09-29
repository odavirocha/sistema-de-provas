package dev.odroca.api_provas.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private String message;
    private String timestamp;
    private String titleError;
    private Integer code;
    
}
