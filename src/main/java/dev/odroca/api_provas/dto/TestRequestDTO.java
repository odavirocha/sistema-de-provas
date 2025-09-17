package dev.odroca.api_provas.dto;

import java.util.List;

import dev.odroca.api_provas.model.QuestionModel;
import lombok.Getter;

@Getter
public class TestRequestDTO {
    
    private String name;
    private List<QuestionModel> questions;

}
