package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmFoundException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private static int id;

    public int generateId() {
        return ++id;
    }

    @Override
    public Film create(Film film) {
        if (films.containsKey(film.getId())) {
            throw new FilmFoundException(String.format("Фильм с id=%d есть в базе", film.getId()));
        }
        int newFilmId = generateId();
        film.setId(newFilmId);
        films.put(newFilmId, film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException(String.format("Фильма с id=%d нет в базе", film.getId()));
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> filmList = new ArrayList<>(films.values());
        return filmList;
    }

    @Override
    public Film getFilmById(Integer id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException(String.format("Фильм с id=%d не найден", id));
        }
        return films.get(id);
    }

    @Override
    public List<Film> getFilmsPopular(Integer count) {
        return getAllFilms()
                .stream()
                .filter(film -> film.getLikes() != null)
                .sorted((t1, t2) -> t2.getLikes().size() - t1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        films.get(filmId).addLike(userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        Film film = films.get(filmId);
        if (!film.getLikes().contains(userId)) {
            throw new NotFoundException("id", String.format("user with id=%d not found", userId));
        }
        film.deleteLike(userId);
    }
}
