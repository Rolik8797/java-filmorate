package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;


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

    public List<Film> getFilms() {
        return films;
    }

    private int getId() {
        return id;
    }
}