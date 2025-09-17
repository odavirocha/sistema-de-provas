package dev.odroca.api_provas.model;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class TestModel {

    private UUID testId;
    private String name;
    private List<QuestionModel> questions;
    
}
