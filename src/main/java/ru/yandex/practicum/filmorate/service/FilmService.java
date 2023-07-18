package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.InvalidReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;


@Component
@Slf4j
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);


    public List<Film> getAllFilms() {
        return filmStorage.getFilms();
    }

    public Film createFilm(Film film) {
        if (filmStorage.getFilms().stream().anyMatch(f -> f.getId() == film.getId())) {
            throw new AlreadyExistsException("Фильм уже есть в базе");
        }
        validateReleaseDate(film, "Добавлен");
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        if (filmStorage.getFilms().stream().noneMatch(f -> f.getId() == film.getId())) {
            throw new NotFoundException("Фильма нет в базе");
        }
        validateReleaseDate(film, "Обновлен");
        return filmStorage.update(film);
    }

    public void validateReleaseDate(Film film, String text) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new InvalidReleaseDateException("Дата релиза не может быть раньше " + MIN_RELEASE_DATE);
        }
        log.debug("{} фильм: {}", text, film.getName());
    }
}