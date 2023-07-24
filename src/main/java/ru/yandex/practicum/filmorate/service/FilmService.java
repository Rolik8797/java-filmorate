package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Integer id) {
        return filmStorage.getFilmById(id);
    }

    public List<Film> getFilmsPopular(Integer count) {
        return filmStorage.getFilmsPopular(count);
    }

    public Film createFilm(Film film) {
        if (filmStorage.filmExistsById(film.getId())) {
            throw new AlreadyExistsException("Фильм уже есть в базе");
        }
        validateReleaseDate(film, "Добавлен");
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        if (!filmStorage.filmExistsById(film.getId())) {
            throw new NotFoundException("Фильма нет в базе");
        }
        validateReleaseDate(film, "Обновлен");
        return filmStorage.update(film);
    }

    public void addLike(Integer filmId, Integer userId) {
        userStorage.getUserById(userId);
        filmStorage.addLike(filmId, userId);
        log.info("like for with id={} added", filmId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        userStorage.getUserById(userId);
        filmStorage.deleteLike(filmId, userId);
        log.info("like for with id={} deleted", filmId);
    }

    private void validateReleaseDate(Film film, String text) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new IncorrectParameterException("Дата релиза не может быть раньше " + MIN_RELEASE_DATE);
        }
        log.debug("{} фильм: {}", text, film.getName());
    }
}