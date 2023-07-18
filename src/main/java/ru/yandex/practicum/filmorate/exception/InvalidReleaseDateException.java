package ru.yandex.practicum.filmorate.exception;

public class InvalidReleaseDateException extends RuntimeException {
    public InvalidReleaseDateException(String message) {
        super(message);
    }
}