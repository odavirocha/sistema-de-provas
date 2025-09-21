package dev.odroca.api_provas.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class QuestionModelDTO {
    private String question;
    private List<OptionModelDTO> options;
}
