package dev.odroca.api_provas.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import dev.odroca.api_provas.dto.TestRequestDTO;
import dev.odroca.api_provas.dto.TestResponseDTO;
import dev.odroca.api_provas.model.TestModel;

@Service
public class TestService {

    public TestResponseDTO createTest(TestRequestDTO test) {

        TestModel testModel = new TestModel();

        testModel.setTestId(UUID.randomUUID());
        testModel.setQuestions(test.getQuestions());
        
        TestResponseDTO response = new TestResponseDTO();

        response.setTestId(testModel.getTestId());
        response.setQuestions(testModel.getQuestions());

        return response;
    }

}
