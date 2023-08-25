package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class GenreService {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Collection<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    public Collection<Genre> getFilmGenres(int filmId) {
        return genreStorage.getGenresByFilmId(filmId);
    }

    public Genre getGenre(Integer supposedId) {
        int genreId = intFromString(supposedId);
        return genreStorage.getGenreById(genreId);
    }

    public boolean deleteFilmGenres(int filmId) {
        return genreStorage.deleteFilmGenres(filmId);
    }

    public boolean addFilmGenres(int filmId, Collection<Genre> genres) {
        return genreStorage.addFilmGenres(filmId, genres);
    }

    private Integer intFromString(final Integer supposedInt) {
        try {
            return Integer.valueOf(supposedInt);
        } catch (NumberFormatException exception) {
            return Integer.MIN_VALUE;
        }
    }
    public Map<Integer, Collection<Genre>> getGenresForFilms(Collection<Integer> filmIds) {
        Map<Integer, Collection<Genre>> filmGenresMap = new HashMap<>();
        for (Integer filmId : filmIds) {
            filmGenresMap.put(filmId, genreStorage.getGenresByFilmId(filmId));
        }
        return filmGenresMap;
    }
}
