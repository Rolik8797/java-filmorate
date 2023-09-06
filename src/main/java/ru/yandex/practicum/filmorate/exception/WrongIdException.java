package ru.yandex.practicum.filmorate.exception;

public class WrongIdException extends RuntimeException {
    public WrongIdException(String message) {
        super(message);
    }
}