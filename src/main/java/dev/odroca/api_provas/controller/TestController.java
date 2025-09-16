package dev.odroca.api_provas.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.odroca.api_provas.dto.TestRequestDTO;
import dev.odroca.api_provas.dto.TestResponseDTO;
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
    public TestResponseDTO postMethodName(@RequestBody TestRequestDTO test) {
        return testService.createTest(test);
    }
    

}
