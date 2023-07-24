package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}