package dev.odroca.api_provas.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.odroca.api_provas.dto.test.CreateTestRequestDTO;
import dev.odroca.api_provas.dto.test.CreateTestResponseDTO;
import dev.odroca.api_provas.entity.TestEntity;
import dev.odroca.api_provas.service.TestService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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

}
