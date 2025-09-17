package dev.odroca.api_provas.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dev.odroca.api_provas.dto.TestRequestDTO;
import dev.odroca.api_provas.dto.TestResponseDTO;
import dev.odroca.api_provas.model.QuestionModel;
import dev.odroca.api_provas.model.TestModel;

@Service
public class TestService {

    public TestResponseDTO createTest(TestRequestDTO test) {

        TestModel testModel = new TestModel();
        
        testModel.setTestId(UUID.randomUUID());
        testModel.setName(test.getName());

        // peek -> altera o valor do stream sem finaliza-lo
        List<QuestionModel> questions = test.getQuestions().stream().peek(question -> {
            question.setId(UUID.randomUUID());
            question.setTestId(testModel.getTestId());
        })
        .collect(Collectors.toList()); // Agrupa todos os valores e retorna uma lista

        testModel.setQuestions(questions);
        
        TestResponseDTO response = new TestResponseDTO();

        response.setTestId(testModel.getTestId());
        response.setName(testModel.getName());
        response.setQuestions(testModel.getQuestions());

        return response;
    }

    

}
