package ru.yandex.practicum.filmorate.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {FilmValidationException.class, UserValidationException.class})
    public ErrorMessage handleException(ConstraintViolationException exception, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        exception.getConstraintViolations().forEach(error -> {
            String fieldName = error.getPropertyPath().toString();
            String errorMessage = error.getMessage() + "Значение: " + error.getInvalidValue().toString();
            errors.put(fieldName, errorMessage);
        });
        ErrorMessage error = new ErrorMessage(
                new Date(),
                HttpStatus.BAD_REQUEST.value(),
                String.valueOf(errors),
                request.getDescription(false));
        log.warn("Ошибка запроса: {}", errors);
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {WrongIdException.class})
    public ErrorMessage handleWrongIdException(Exception exception, WebRequest request) {
        ErrorMessage error = new ErrorMessage(
                new Date(),
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                request.getDescription(false));
        log.warn("Ошибка запроса: {} {}", exception.getMessage(), request.getDescription(false));
        return error;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {NotFoundException.class})
    public ErrorMessage handleNotFoundException(Exception exception, WebRequest request) {
        ErrorMessage error = new ErrorMessage(
                new Date(),
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                request.getDescription(false));
        log.warn("Ошибка запроса: {} {}", exception.getMessage(), request.getDescription(false));
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ErrorMessage handleValidationException(MethodArgumentNotValidException exception,
                                                  WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ErrorMessage error = new ErrorMessage(
                new Date(),
                HttpStatus.BAD_REQUEST.value(),
                String.valueOf(errors),
                request.getDescription(false)
        );
        log.warn("Ошибка запроса: {}", errors);
        return error;
    }
}
