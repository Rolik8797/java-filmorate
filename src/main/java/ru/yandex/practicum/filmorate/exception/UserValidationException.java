package ru.yandex.practicum.filmorate.exception;

import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

public class UserValidationException extends ConstraintViolationException {
    public UserValidationException(String message, Set<ConstraintViolation<User>> violations) {

        super(message, violations);
    }
}