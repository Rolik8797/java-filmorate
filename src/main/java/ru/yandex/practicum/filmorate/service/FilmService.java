package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Map;

@Component
@Slf4j
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private static final LocalDate START_DATA = LocalDate.of(1895, 12, 28);

    public Map<Integer, Film> getAllFilms() {

        return filmStorage.getFilms();
    }

    public Film createFilm(Film film) {

        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {

        return filmStorage.update(film);
    }

    public void validateReleaseDate(Film film, String text) {
        if (film.getReleaseDate().isBefore(START_DATA)) {
            throw new ValidationException("Дата релиза не может быть раньше " + START_DATA);
        }
        log.debug("{} фильм: ", text, film.getName());
    }
}
