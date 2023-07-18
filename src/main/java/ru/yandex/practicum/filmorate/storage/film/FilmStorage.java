package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class FilmStorage {
    private final List<Film> films = new ArrayList<>();
    private static int id;

    public int generateId() {
        return ++id;
    }

    public Film create(Film film) {
        int newFilmId = generateId();
        film.setId(newFilmId);
        films.add(film);
        return film;
    }

    public Film update(Film film) {
        int filmIdToUpdate = film.getId();
        for (int i = 0; i < films.size(); i++) {
            Film storedFilm = films.get(i);
            if (storedFilm.getId() == filmIdToUpdate) {
                films.set(i, film);
                return film;
            }
        }
        throw new IllegalArgumentException("Film with ID " + filmIdToUpdate + " not found.");
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

    public List<Film> getFilms() {
        return films;
    }

    private int getId() {
        return id;
    }
}