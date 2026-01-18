package dev.odroca.api_provas.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import dev.odroca.api_provas.exception.QuestionNotFoundException;
import dev.odroca.api_provas.exception.OptionNotFoundException;
import dev.odroca.api_provas.exception.InvalidCredentialsException;
import dev.odroca.api_provas.exception.InvalidTokenException;
import dev.odroca.api_provas.error.ErrorResponse;
import dev.odroca.api_provas.exception.CorrectOptionNotFoundException;
import dev.odroca.api_provas.exception.EmailAlreadyExistsException;
import dev.odroca.api_provas.exception.MultipleCorrectOptionsException;
import dev.odroca.api_provas.exception.TestNotFoundException;
import dev.odroca.api_provas.exception.TestNullNameException;
import dev.odroca.api_provas.exception.UnauthorizedException;
import dev.odroca.api_provas.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericError(Exception ex) {

        log.error("Erro inesperado: ", ex);

        return new ErrorResponse(
            "Erro inesperado ao processar sua solicitação. Tente novamente mais tarde.", 
            LocalDateTime.now().toString(), 
            "InternalServerError", 
            500);
    }
    
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoResourceFoundException(NoResourceFoundException ex) {
        log.error("Endpoint não existe: ", ex);
        return new ErrorResponse("Endpoint não encontrado", LocalDateTime.now().toString(), "NotFound", 404);
    }

    @ExceptionHandler(TestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleTestNotFoundException(TestNotFoundException ex) {
        log.error("Prova não encontrada: ", ex);
        return new ErrorResponse(ex.getMessage(), LocalDateTime.now().toString(), "NotFound", 404);
    }
    
    @ExceptionHandler(QuestionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleQuestionNotFoundException(QuestionNotFoundException ex) {
        log.error("Questão não encontrada: ", ex);
        return new ErrorResponse(ex.getMessage(), LocalDateTime.now().toString(), "NotFound", 404);
    }
    
    @ExceptionHandler(OptionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleOptionNotFoundException(OptionNotFoundException ex) {
        log.error("Opção não encontrada: ", ex);
        return new ErrorResponse(ex.getMessage(), LocalDateTime.now().toString(), "NotFound", 404);
    }
    
    @ExceptionHandler(CorrectOptionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCorrectOptionNotFoundException(CorrectOptionNotFoundException ex) {
        log.error("Opção correta não encontrada: ", ex);
        return new ErrorResponse(ex.getMessage(), LocalDateTime.now().toString(), "NotFound", 404);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException ex) {
        log.error("Usuário não encontrado: ", ex);
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
        log.error("Prova com nome nulo: ", ex);
        return new ErrorResponse(ex.getMessage(), LocalDateTime.now().toString(), "BadRequest", 400);
    }
    
    @ExceptionHandler(MultipleCorrectOptionsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMultipleCorrectOptionsException(MultipleCorrectOptionsException ex) {
        log.error("Mais de uma opção correta marcada: ", ex);
        return new ErrorResponse(ex.getMessage(), LocalDateTime.now().toString(), "BadRequest", 400);
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidTokenException(InvalidTokenException ex) {
        return new ErrorResponse(ex.getMessage(), LocalDateTime.now().toString(), "BadRequest", 400);
    }
    
    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return new ErrorResponse(ex.getMessage(), LocalDateTime.now().toString(), "Unauthorized", 401);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorizedException(UnauthorizedException ex) {
        log.error("Usuário não autorizado: ", ex);
        return new ErrorResponse(ex.getMessage(), LocalDateTime.now().toString(), "Unauthorized", 401);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return new ErrorResponse(ex.getMessage(), LocalDateTime.now().toString(), "Conflict", 409);
    }

}
