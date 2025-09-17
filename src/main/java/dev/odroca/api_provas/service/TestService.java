package dev.odroca.api_provas.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.aspectj.weaver.ast.Test;
import org.springframework.stereotype.Service;

import dev.odroca.api_provas.dto.TestRequestDTO;
import dev.odroca.api_provas.dto.TestResponseDTO;
import dev.odroca.api_provas.entity.TestEntity;
import dev.odroca.api_provas.model.QuestionModel;
import dev.odroca.api_provas.model.TestModel;
import dev.odroca.api_provas.repository.TestRepository;

@Service
public class TestService {
    private final TestRepository testRepository;

    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public TestResponseDTO createTest(TestEntity test) {

        TestEntity saved = testRepository.save(test);

        // peek -> altera o valor do stream sem finaliza-lo
        // List<QuestionModel> questions = test.getQuestions().stream().peek(question -> {
        //     question.setId(UUID.randomUUID());
        //     question.setTestId(testModel.getTestId());
        // })
        // .collect(Collectors.toList()); // Agrupa todos os valores e retorna uma lista

        // testModel.setQuestions(questions);
        
        TestResponseDTO response = new TestResponseDTO();

        response.setTestId(saved.getId());
        response.setName(saved.getName());

        return response;
    }

    public void createQuestion(QuestionEntity questionEntity) {}
    
    public QuestionsResponseDTO createQuestions() {}

}
