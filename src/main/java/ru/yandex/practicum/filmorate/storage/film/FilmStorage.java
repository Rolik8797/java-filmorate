package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private static int id;

    public int generateId() {
        return ++id;
    }

    public Film create(Film film) {
        int newTaskId = generateId();
        film.setId(newTaskId);
        films.put(newTaskId, film);
        return film;
    }

    public Film update(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilmStorage that = (FilmStorage) o;
        return Objects.equals(films, that.films);
    }

    @Override
    public int hashCode() {
        return Objects.hash(films);
    }

    @Override
    public String toString() {
        return "FilmStorage{" +
                "films=" + films +
                '}';
    }

    public Map<Integer, Film> getFilms() {
        return films;
    }

    public static int getId() {
        return id;
    }
}

