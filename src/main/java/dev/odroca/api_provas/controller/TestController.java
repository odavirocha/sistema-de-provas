package dev.odroca.api_provas.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.odroca.api_provas.dto.test.AnswerTestRequestDTO;
import dev.odroca.api_provas.dto.test.AnswerTestResponseDTO;
import dev.odroca.api_provas.dto.test.CreateTestRequestDTO;
import dev.odroca.api_provas.dto.test.CreateTestResponseDTO;
import dev.odroca.api_provas.dto.test.DeleteTestResponseDTO;
import dev.odroca.api_provas.entity.TestEntity;
import dev.odroca.api_provas.service.TestService;
import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping("/")
    public CreateTestResponseDTO createTest(@RequestBody @Valid CreateTestRequestDTO test) {

        TestEntity testEntity = new TestEntity();
        testEntity.setName(test.getName());

        return testService.createTest(testEntity);
    }

    @PostMapping("/{testId}")
    public AnswerTestResponseDTO answerTest(@PathVariable UUID testId, @RequestBody AnswerTestRequestDTO test) {
        return testService.answerTest(testId, test);
    }
    

    @DeleteMapping("/{testId}")
    public DeleteTestResponseDTO deleteTest(@PathVariable UUID testId) {
        return testService.deleteTest(testId);
    }

}
