package dev.odroca.api_provas.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.odroca.api_provas.exception.QuestionNotFoundException;
import dev.odroca.api_provas.exception.OptionNotFoundException;
import dev.odroca.api_provas.error.ErrorResponse;
import dev.odroca.api_provas.exception.CorrectOptionNotFoundException;
import dev.odroca.api_provas.exception.TestNotFoundException;

@RestControllerAdvice
public class ApplicationControllerAdvice {
    
    @ExceptionHandler(TestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleTestNotFoundException(TestNotFoundException ex) {
        return new ErrorResponse(ex.getMessage(), LocalDateTime.now().toString(), "NotFound", 404);
    }
    
    @ExceptionHandler(QuestionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleQuestionNotFoundException(QuestionNotFoundException ex) {
        return new ErrorResponse(ex.getMessage(), LocalDateTime.now().toString(), "NotFound", 404);
    }
    
    @ExceptionHandler(OptionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleOptionNotFoundException(OptionNotFoundException ex) {
        return new ErrorResponse(ex.getMessage(), LocalDateTime.now().toString(), "NotFound", 404);
    }
    
    @ExceptionHandler(CorrectOptionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCorrectOptionNotFoundException(CorrectOptionNotFoundException ex) {
        return new ErrorResponse(ex.getMessage(), LocalDateTime.now().toString(), "NotFound", 404);
    }
    
}
