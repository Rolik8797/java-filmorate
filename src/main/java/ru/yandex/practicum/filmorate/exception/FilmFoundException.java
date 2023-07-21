package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FOUND)
public class FilmFoundException extends RuntimeException {
    public FilmFoundException(String message) {
        super(message);
    }
}
