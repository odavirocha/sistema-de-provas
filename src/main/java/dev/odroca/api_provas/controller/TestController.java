package dev.odroca.api_provas.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.odroca.api_provas.dto.test.AnswerTestRequestDTO;
import dev.odroca.api_provas.dto.test.AnswerTestResponseDTO;
import dev.odroca.api_provas.dto.test.CreateTestRequestDTO;
import dev.odroca.api_provas.dto.test.TestResponseDTO;
import dev.odroca.api_provas.dto.test.DeleteTestResponseDTO;
import dev.odroca.api_provas.entity.TestEntity;
import dev.odroca.api_provas.entity.UserEntity;
import dev.odroca.api_provas.exception.UserNotFoundException;
import dev.odroca.api_provas.repository.UserRepository;
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
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity<TestResponseDTO> createTest(@RequestBody @Valid CreateTestRequestDTO test, @AuthenticationPrincipal Jwt jwt) {

        TestEntity testEntity = new TestEntity();
        testEntity.setName(test.getName());

        UserEntity user = userRepository.findById(UUID.fromString(jwt.getSubject())).orElseThrow(() -> new UserNotFoundException());

        testEntity.setUser(user);

        TestResponseDTO response = testService.createTest(testEntity);
        return new ResponseEntity<TestResponseDTO>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{testId}")
    public ResponseEntity<AnswerTestResponseDTO> answerTest(@PathVariable UUID testId, @RequestBody AnswerTestRequestDTO test) {
        AnswerTestResponseDTO response = testService.answerTest(testId, test);
        return new ResponseEntity<AnswerTestResponseDTO>(response, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<TestResponseDTO>> getAllTestsForUser(@PathVariable UUID userId, HttpServletRequest request) {
        List<TestResponseDTO> response = testService.getAllTestsForUser(userId, request);
        return new ResponseEntity<List<TestResponseDTO>>(response, HttpStatus.OK);
    }
    

    @DeleteMapping("/{testId}")
    public ResponseEntity<DeleteTestResponseDTO> deleteTest(@PathVariable UUID testId) {
        DeleteTestResponseDTO response = testService.deleteTest(testId);
        return new ResponseEntity<DeleteTestResponseDTO>(response, HttpStatus.OK);
    }

}
