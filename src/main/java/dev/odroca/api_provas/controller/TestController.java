package dev.odroca.api_provas.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.odroca.api_provas.dto.test.AnswerTestRequestDTO;
import dev.odroca.api_provas.dto.test.AnswerTestResponseDTO;
import dev.odroca.api_provas.dto.test.CreateTestRequestDTO;
import dev.odroca.api_provas.dto.test.TestForGetTestsResponseDTO;
import dev.odroca.api_provas.dto.test.DeleteTestResponseDTO;
import dev.odroca.api_provas.dto.test.TestForGetTestResponseDTO;
import dev.odroca.api_provas.entity.TestEntity;
import dev.odroca.api_provas.service.TestService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<TestForGetTestsResponseDTO> createTest(@RequestBody @Valid CreateTestRequestDTO test, @AuthenticationPrincipal Jwt jwt) {

        TestEntity testEntity = new TestEntity();
        testEntity.setName(test.name());
        testEntity.setUserId(UUID.fromString(jwt.getSubject()));

        TestForGetTestsResponseDTO response = testService.createTest(testEntity);
        return new ResponseEntity<TestForGetTestsResponseDTO>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{testId}")
    public ResponseEntity<AnswerTestResponseDTO> answerTest(@PathVariable UUID testId, @RequestBody AnswerTestRequestDTO test, @AuthenticationPrincipal Jwt jwt) {
        AnswerTestResponseDTO response = testService.answerTest(testId, test, UUID.fromString(jwt.getSubject()));
        return new ResponseEntity<AnswerTestResponseDTO>(response, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<TestForGetTestsResponseDTO>> getAllTestsForUser(HttpServletRequest request) {
        List<TestForGetTestsResponseDTO> response = testService.getAllTestsForUser(request);
        return new ResponseEntity<List<TestForGetTestsResponseDTO>>(response, HttpStatus.OK);
    }
    
    @GetMapping("/{testId}")
    public ResponseEntity<TestForGetTestResponseDTO> getTest(@PathVariable UUID testId, HttpServletRequest request) {
        TestForGetTestResponseDTO response = testService.getTest(testId, request);
        return new ResponseEntity<TestForGetTestResponseDTO>(response, HttpStatus.OK);
    }
    

    @DeleteMapping("/{testId}")
    public ResponseEntity<DeleteTestResponseDTO> deleteTest(@PathVariable UUID testId, HttpServletRequest request) {
        DeleteTestResponseDTO response = testService.deleteTest(testId, request);
        return new ResponseEntity<DeleteTestResponseDTO>(response, HttpStatus.OK);
    }

}
