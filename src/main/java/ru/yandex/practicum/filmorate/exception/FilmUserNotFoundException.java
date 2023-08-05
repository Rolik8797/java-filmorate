package ru.yandex.practicum.filmorate.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FilmUserNotFoundException extends RuntimeException {
    public FilmUserNotFoundException(String message) {
        super(message);
    }
}