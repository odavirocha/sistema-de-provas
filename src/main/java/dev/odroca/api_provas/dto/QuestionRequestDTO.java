package dev.odroca.api_provas.dto;

import java.util.UUID;

import lombok.Getter;

@Getter
public class QuestionRequestDTO {
    UUID testId;
    QuestionModelDTO question;
}
