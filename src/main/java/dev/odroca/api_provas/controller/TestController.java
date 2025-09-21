package dev.odroca.api_provas.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.odroca.api_provas.dto.QuestionRequestDTO;
import dev.odroca.api_provas.dto.QuestionResponseDTO;
import dev.odroca.api_provas.dto.QuestionsRequestDTO;
import dev.odroca.api_provas.dto.QuestionsResponseDTO;
import dev.odroca.api_provas.dto.TestRequestDTO;
import dev.odroca.api_provas.dto.TestResponseDTO;
import dev.odroca.api_provas.entity.TestEntity;
import dev.odroca.api_provas.service.TestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("")
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping("/create-test")
    public TestResponseDTO createTest(@RequestBody TestRequestDTO test) {

        TestEntity testEntity = new TestEntity();
        testEntity.setName(test.getName());

        return testService.createTest(testEntity);
    }
    
    @PostMapping("/create-question")
    public QuestionResponseDTO createQuestion(@RequestBody QuestionRequestDTO question) {
        return testService.createQuestion(question.getTestId(), question.getQuestion());
    }
    
    @PostMapping("create-questions")
    public QuestionsResponseDTO createQuestions(@RequestBody QuestionsRequestDTO questions) {
        return testService.createQuestions(questions);        
    }
    

}
