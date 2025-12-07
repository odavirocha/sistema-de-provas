package dev.odroca.api_provas.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.odroca.api_provas.dto.question.CreateQuestionRequestDTO;
import dev.odroca.api_provas.dto.question.CreateQuestionResponseDTO;
import dev.odroca.api_provas.dto.question.DeleteQuestionResponseDTO;
import dev.odroca.api_provas.dto.question.GetQuestionModelDTO;
import dev.odroca.api_provas.dto.question.UpdateQuestionRequestDTO;
import dev.odroca.api_provas.dto.question.UpdateQuestionResponseDTO;
import dev.odroca.api_provas.dto.questions.CreateQuestionsRequestDTO;
import dev.odroca.api_provas.dto.questions.CreateQuestionsResponseDTO;
import dev.odroca.api_provas.service.QuestionService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;
        
    @PostMapping("/{testId}")
    public ResponseEntity<CreateQuestionResponseDTO> createQuestion(@PathVariable UUID testId, @RequestBody @Valid CreateQuestionRequestDTO question, @AuthenticationPrincipal Jwt jwt) {
        CreateQuestionResponseDTO response = questionService.createQuestion(testId, question.question(), UUID.fromString(jwt.getSubject()));
        return new ResponseEntity<CreateQuestionResponseDTO>(response, HttpStatus.CREATED);
    }
    
    @PostMapping("/{testId}/batch")
    public ResponseEntity<CreateQuestionsResponseDTO> createQuestions(@PathVariable UUID testId, @RequestBody @Valid CreateQuestionsRequestDTO questions, @AuthenticationPrincipal Jwt jwt) {
        CreateQuestionsResponseDTO response = questionService.createQuestions(testId, questions, UUID.fromString(jwt.getSubject()));        
        return new ResponseEntity<CreateQuestionsResponseDTO>(response, HttpStatus.OK);
    }
    
    @PutMapping("/{questionId}")
    public ResponseEntity<UpdateQuestionResponseDTO> updateQuestion(@PathVariable UUID questionId, @RequestBody @Valid UpdateQuestionRequestDTO questionUpdate, @AuthenticationPrincipal Jwt jwt) {
        UpdateQuestionResponseDTO response = questionService.updateQuestion(questionId, questionUpdate, UUID.fromString(jwt.getSubject()));
        return new ResponseEntity<UpdateQuestionResponseDTO>(response, HttpStatus.OK);
    }

    @GetMapping("/{testId}")
    public ResponseEntity<List<GetQuestionModelDTO>> getAllQuestionsForTest(@PathVariable UUID testId, @AuthenticationPrincipal Jwt jwt) {
        List<GetQuestionModelDTO> response = questionService.getAllQuestionsForTest(testId, UUID.fromString(jwt.getSubject()));
        return new ResponseEntity<List<GetQuestionModelDTO>>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<DeleteQuestionResponseDTO> deleteQuestion(@PathVariable UUID questionId, @AuthenticationPrincipal Jwt jwt) {
        DeleteQuestionResponseDTO response = questionService.deleteQuestion(questionId, UUID.fromString(jwt.getSubject()));
        return new ResponseEntity<DeleteQuestionResponseDTO>(response, HttpStatus.OK);
    }
    
}
