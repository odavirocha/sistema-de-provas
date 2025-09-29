package dev.odroca.api_provas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.odroca.api_provas.exception.QuestionNotFoundException;
import dev.odroca.api_provas.exception.OptionNotFoundException;
import dev.odroca.api_provas.exception.CorrectOptionNotFoundException;
import dev.odroca.api_provas.exception.TestNotFoundException;

@RestControllerAdvice
public class ApplicationControllerAdvice {
    
    @ExceptionHandler(TestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleTestNotFoundException(TestNotFoundException ex) {
        return ex.getMessage();
    }
    
    @ExceptionHandler(QuestionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleQuestionNotFoundException(QuestionNotFoundException ex) {
        return ex.getMessage();
    }
    
    @ExceptionHandler(OptionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleOptionNotFoundException(OptionNotFoundException ex) {
        return ex.getMessage();
    }
    
    @ExceptionHandler(CorrectOptionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleCorrectOptionNotFoundException(CorrectOptionNotFoundException ex) {
        return ex.getMessage();
    }
    
}
