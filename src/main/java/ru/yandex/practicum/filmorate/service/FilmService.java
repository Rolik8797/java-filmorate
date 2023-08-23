package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;


import java.util.Collection;


@Service
public class FilmService {
    private static int increment = 0;
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(@Qualifier("DBFilmStorage") FilmStorage filmStorage,
                       UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Collection<Film> getFilms() {
        return filmStorage.getAllFilms();
    }

    public Film add(FilmDto filmDto) {
        Film film = convertToFilm(filmDto);
        return filmStorage.addFilm(film);
    }

    public Film update(FilmDto filmDto) {
        Film film = convertToFilm(filmDto);
        return filmStorage.updateFilm(film);
    }

    public void addLike(final String id, final String userId) {
        Film film = getStoredFilm(id);
        User user = userService.getUser(userId);
        filmStorage.addLike(film.getId(), user.getId());
    }

    public void deleteLike(final String id, final String userId) {
        Film film = getStoredFilm(id);
        User user = userService.getUser(userId);
        filmStorage.deleteLike(film.getId(), user.getId());
    }

    public Collection<Film> getMostPopularFilms(final Integer count) {
        Integer size = count != null ? count : 10;
        return filmStorage.getMostPopularFilms(size);
    }

    public Film getFilm(String id) {
        return getStoredFilm(id);
    }

    private Film convertToFilm(FilmDto filmDto) {
        Film film = new Film();
        film.setId(filmDto.getId());
        film.setName(filmDto.getName());
        film.setDescription(filmDto.getDescription());
        film.setReleaseDate(filmDto.getReleaseDate());
        film.setDuration(filmDto.getDuration());
        film.setRate(filmDto.getRate());
        film.setMpa(filmDto.getMpa());
        film.setGenres(filmDto.getGenres());
        return film;
    }

    private Film getStoredFilm(final String supposedId) {
        int filmId = intFromString(supposedId);
        if (filmId == Integer.MIN_VALUE) {
            throw new WrongIdException("Не удалось распознать идентификатор фильма: " + "значение " + supposedId);
        }

        Film film = filmStorage.getFilm(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с идентификатором " + filmId + " не зарегистрирован!");
        }

        return film;
    }

    private Integer intFromString(final String supposedInt) {
        try {
            return Integer.valueOf(supposedInt);
        } catch (NumberFormatException exception) {
            return Integer.MIN_VALUE;
        }
    }
}