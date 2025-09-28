package dev.odroca.api_provas.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.odroca.api_provas.dto.CreateQuestionRequestDTO;
import dev.odroca.api_provas.dto.CreateQuestionResponseDTO;
import dev.odroca.api_provas.dto.CreateQuestionsRequestDTO;
import dev.odroca.api_provas.dto.CreateQuestionsResponseDTO;
import dev.odroca.api_provas.dto.GetQuestionModelDTO;
import dev.odroca.api_provas.dto.UpdateQuestionRequestDTO;
import dev.odroca.api_provas.dto.UpdateQuestionResponseDTO;
import dev.odroca.api_provas.service.QuestionService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;
        
    @PostMapping("/questions")
    public CreateQuestionResponseDTO createQuestion(@RequestBody @Valid CreateQuestionRequestDTO question) {
        return questionService.createQuestion(question.getTestId(), question.getQuestion());
    }
    
    @PostMapping("/questions/batch")
    public CreateQuestionsResponseDTO createQuestions(@RequestBody @Valid CreateQuestionsRequestDTO questions) {
        return questionService.createQuestions(questions);        
    }
    
    @PutMapping("/questions/{questionId}")
    public UpdateQuestionResponseDTO editQuestion(@PathVariable UUID questionId, @RequestBody @Valid UpdateQuestionRequestDTO questionUpdate) {
        return questionService.updateQuestion(questionId, questionUpdate);
    }

    @GetMapping("questions/{testId}")
    public List<GetQuestionModelDTO> getAllQuestionsForTest(@PathVariable UUID testId) {
        return questionService.getAllQuestionsForTest(testId);
    }
    
}
