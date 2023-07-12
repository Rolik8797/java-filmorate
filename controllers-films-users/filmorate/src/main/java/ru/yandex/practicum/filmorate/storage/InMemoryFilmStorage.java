package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film getFilm(int filmId) {
        return  films.get(filmId);
    }
    @Override
    public Collection<Film> getAllFilms() {
        Collection<Film> allFilms = films.values();
        if (allFilms.isEmpty()) {
            allFilms.addAll(films.values());
        }
        return allFilms;
    }
    @Override
    public Film addFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }
    @Override
    public Film updateFilm (Film film) {
        if (!getAllFilms().contains(film)) {
            throw  new NotFoundException("Фильм с идентификаторм " +
                    film.getId() + " не зарегистрирован");
        }
        films.put(film.getId(), film);
        return film;
    }
    @Override
    public boolean deleteFilm(Film film) {
        films.remove(film.getId());
        return true;
    }

}
