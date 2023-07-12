package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.WrongIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Collection;
import java.util.Set;

@Service
public class FilmService {
    private static int increment = 0;

    private final Validator validator;
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService (Validator validator, @Qualifier("DBFilmStorage") FilmStorage filmStorage,
                        @Autowired(required = false) UserService userService) {
        this.validator = validator;
        this.filmStorage = filmStorage;
        this.userService = userService;
    }
    public Collection<Film> getFilms() {
        return filmStorage.getAllFilms();
    }
    public Film add(Film film) {
        validate (film);
        return filmStorage.addFilm(film);
    }
    public Film update(Film film) {
        validate (film);
        return filmStorage.updateFilm(film);
    }
    public Film getFilm(String id) {
        return getStoredFilm(id);
    }
    private void validate (Film film) {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        if (!violations.isEmpty()){
            StringBuilder messageBuilder = new StringBuilder();
            for (ConstraintViolation<Film> filmConstraintViolation : violations) {
                messageBuilder.append(filmConstraintViolation.getMessage());
            }
            throw new FilmValidationException("Ошибка валидации фильма: " + messageBuilder, violations);
        }
        if (film.getId() == 0) {
            film.setId(getNextId());
        }
    }
    private static  int getNextId(){
        return ++increment;
    }
    private Integer intFromString(final String supposeInt) {
        try {
            return Integer.valueOf(supposeInt);
        } catch (NumberFormatException exception){
            return Integer.MIN_VALUE;
        }
    }
    private Film getStoredFilm(final String supposedId) {
        final int filmId = intFromString(supposedId);
        if (filmId == Integer.MIN_VALUE) {
            throw new WrongIdException("Не удалось распознать идентификатор фильма: " +
                    "значение " + supposedId);
        }
        Film film = filmStorage.getFilm(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с идентификатором " +
                    filmId + " не зарегистрирован");
        }
        return film;
    }
}
