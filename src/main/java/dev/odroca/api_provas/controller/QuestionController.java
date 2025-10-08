package dev.odroca.api_provas.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.odroca.api_provas.dto.question.CreateQuestionRequestDTO;
import dev.odroca.api_provas.dto.question.CreateQuestionResponseDTO;
import dev.odroca.api_provas.dto.question.GetQuestionModelDTO;
import dev.odroca.api_provas.dto.question.UpdateQuestionRequestDTO;
import dev.odroca.api_provas.dto.question.UpdateQuestionResponseDTO;
import dev.odroca.api_provas.dto.questions.CreateQuestionsRequestDTO;
import dev.odroca.api_provas.dto.questions.CreateQuestionsResponseDTO;
import dev.odroca.api_provas.service.QuestionService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;
        
    @PostMapping("/")
    public ResponseEntity<CreateQuestionResponseDTO> createQuestion(@RequestBody @Valid CreateQuestionRequestDTO question) {
        CreateQuestionResponseDTO response = questionService.createQuestion(question.getTestId(), question.getQuestion());
        return new ResponseEntity<CreateQuestionResponseDTO>(response, HttpStatus.CREATED);
    }
    
    // testar a resposta de quando envia mais de uma questão
    @PostMapping("/batch")
    public ResponseEntity<CreateQuestionsResponseDTO> createQuestions(@RequestBody @Valid CreateQuestionsRequestDTO questions) {
        CreateQuestionsResponseDTO response = questionService.createQuestions(questions);        
        return new ResponseEntity<CreateQuestionsResponseDTO>(response, HttpStatus.OK);
    }
    
    @PutMapping("/{questionId}")
    public ResponseEntity<UpdateQuestionResponseDTO> editQuestion(@PathVariable UUID questionId, @RequestBody @Valid UpdateQuestionRequestDTO questionUpdate) {
        UpdateQuestionResponseDTO response = questionService.updateQuestion(questionId, questionUpdate);
        return new ResponseEntity<UpdateQuestionResponseDTO>(response, HttpStatus.OK);
    }

    @GetMapping("/{testId}")
    public ResponseEntity<List<GetQuestionModelDTO>> getAllQuestionsForTest(@PathVariable UUID testId) {
        List<GetQuestionModelDTO> response = questionService.getAllQuestionsForTest(testId);
        return new ResponseEntity<List<GetQuestionModelDTO>>(response, HttpStatus.OK);
    }
    
}
