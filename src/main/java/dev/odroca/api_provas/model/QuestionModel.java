package dev.odroca.api_provas.model;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class QuestionModel {
    UUID id;
    UUID testId; // chave estrangeira
    String answer;
    List<String> options;
    String correctOptio;
}
