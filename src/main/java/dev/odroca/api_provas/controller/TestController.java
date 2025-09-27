package dev.odroca.api_provas.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.odroca.api_provas.dto.CreateQuestionRequestDTO;
import dev.odroca.api_provas.dto.CreateQuestionResponseDTO;
import dev.odroca.api_provas.dto.CreateQuestionsRequestDTO;
import dev.odroca.api_provas.dto.CreateQuestionsResponseDTO;
import dev.odroca.api_provas.dto.CreateTestRequestDTO;
import dev.odroca.api_provas.dto.CreateTestResponseDTO;
import dev.odroca.api_provas.dto.UpdateQuestionRequestDTO;
import dev.odroca.api_provas.dto.UpdateQuestionResponseDTO;
import dev.odroca.api_provas.entity.TestEntity;
import dev.odroca.api_provas.service.TestService;
import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("")
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping("/test")
    public CreateTestResponseDTO createTest(@RequestBody @Valid CreateTestRequestDTO test) {

        TestEntity testEntity = new TestEntity();
        testEntity.setName(test.getName());

        return testService.createTest(testEntity);
    }
    
    @PostMapping("/questions")
    public CreateQuestionResponseDTO createQuestion(@RequestBody @Valid CreateQuestionRequestDTO question) {
        return testService.createQuestion(question.getTestId(), question.getQuestion());
    }
    
    @PostMapping("/questions/batch")
    public CreateQuestionsResponseDTO createQuestions(@RequestBody @Valid CreateQuestionsRequestDTO questions) {
        return testService.createQuestions(questions);        
    }
    
    @PutMapping("/questions/{questionId}")
    public UpdateQuestionResponseDTO editQuestion(@PathVariable UUID questionId, @RequestBody @Valid UpdateQuestionRequestDTO questionUpdate) {
        return testService.updateQuestion(questionId, questionUpdate);
    }

}
