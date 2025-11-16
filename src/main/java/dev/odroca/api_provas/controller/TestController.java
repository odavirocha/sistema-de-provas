package dev.odroca.api_provas.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.odroca.api_provas.dto.test.AnswerTestRequestDTO;
import dev.odroca.api_provas.dto.test.AnswerTestResponseDTO;
import dev.odroca.api_provas.dto.test.CreateTestRequestDTO;
import dev.odroca.api_provas.dto.test.TestResponseDTO;
import dev.odroca.api_provas.dto.test.DeleteTestResponseDTO;
import dev.odroca.api_provas.entity.TestEntity;
import dev.odroca.api_provas.model.TestModelDTO;
import dev.odroca.api_provas.service.TestService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping("/")
    public ResponseEntity<TestResponseDTO> createTest(@RequestBody @Valid CreateTestRequestDTO test, @AuthenticationPrincipal Jwt jwt) {

        TestEntity testEntity = new TestEntity();
        testEntity.setName(test.getName());
        testEntity.setUserId(UUID.fromString(jwt.getSubject()));

        TestResponseDTO response = testService.createTest(testEntity);
        return new ResponseEntity<TestResponseDTO>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{testId}")
    public ResponseEntity<AnswerTestResponseDTO> answerTest(@PathVariable UUID testId, @RequestBody AnswerTestRequestDTO test) {
        AnswerTestResponseDTO response = testService.answerTest(testId, test);
        return new ResponseEntity<AnswerTestResponseDTO>(response, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<TestModelDTO>> getAllTestsForUser(@PathVariable UUID userId) {
        List<TestModelDTO> response = testService.getAllTestsForUser(userId);
        return new ResponseEntity<List<TestModelDTO>>(response, HttpStatus.OK);
    }
    

    @DeleteMapping("/{testId}")
    public ResponseEntity<DeleteTestResponseDTO> deleteTest(@PathVariable UUID testId) {
        DeleteTestResponseDTO response = testService.deleteTest(testId);
        return new ResponseEntity<DeleteTestResponseDTO>(response, HttpStatus.OK);
    }

}
