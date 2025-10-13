package dev.odroca.api_provas.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.odroca.api_provas.exception.QuestionNotFoundException;
import dev.odroca.api_provas.exception.OptionNotFoundException;
import dev.odroca.api_provas.error.ErrorResponse;
import dev.odroca.api_provas.exception.CorrectOptionNotFoundException;
import dev.odroca.api_provas.exception.MultipleCorrectOptionsException;
import dev.odroca.api_provas.exception.TestNotFoundException;
import dev.odroca.api_provas.exception.TestNullNameException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericError(Exception e) {

        log.error("Erro inesperado: " + e);

        return new ErrorResponse(
            "Erro inesperado ao processar sua solicitação. Tente novamente mais tarde.", 
            LocalDateTime.now().toString(), 
            "InternalServerError", 
            500);
    }
    
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
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
        String defaultMessage = ex.getBindingResult().getFieldErrors().stream().findFirst().map(FieldError -> FieldError.getDefaultMessage()).orElse("Erro de validação");
        return new ErrorResponse(defaultMessage, LocalDateTime.now().toString(), "BadRequest", 400);
    }
    
    @ExceptionHandler(TestNullNameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTestNameNullException(TestNullNameException ex) {
        return new ErrorResponse(ex.getMessage(), LocalDateTime.now().toString(), "BadRequest", 400);
    }
    
    @ExceptionHandler(MultipleCorrectOptionsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTestNameNullException(MultipleCorrectOptionsException ex) {
        return new ErrorResponse(ex.getMessage(), LocalDateTime.now().toString(), "BadRequest", 400);
    }
    
}
