package dev.odroca.api_provas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateTestRequestDTO {
    
    @NotBlank
    @NotNull
    @NotEmpty
    private String name;

}
