package ru.yandex.practicum.filmorate.storage.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.*;
import java.util.Collection;
import java.util.Objects;

@Component("DBFilmStorage")
public class DBFilmStorage implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(DBFilmStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public DBFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Film getFilm(int filmId) {
        String sqlFilm = "select * from FILM " +
                "INNER JOIN RATINGMPA R on FILM.RATINGID = R.RATINGID " +
                "where FILMID = ?";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlFilm, (rs, rowNum) -> makeFilm(rs), filmId);
        }
        catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильм с индентификатором " +
                    filmId + " не зарегистрирован!");
        }
        log.info("Найден фильм: {} {}", film.getId(), film.getName());
        return film;
    }
    @Override
    public Collection<Film> getAllFilms() {
        String sql = "select * from FILM " +
                "INNER JOIN RATINGMPA R on FILM.RATINGID = R.RATINGID ";
        return  jdbcTemplate.query(sql, (resultSet, rowNum) -> makeFilm(resultSet));
    }
    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "insert into FILM " +
                "(NAME, DESCRIPTION, RELEASEDATE, DURATION, RATINGID) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, film.getName());
            preparedStatement.setString(2, film.getDescription());
            preparedStatement.setDate(3, Date.valueOf(film.getReleaseDate()));
            preparedStatement.setLong(4, film.getDuration());
            return preparedStatement;
        }, keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();

        return  getFilm(id);
    }
    @Override
    public Film updateFilm (Film film) {
        String sqlQuery = "update FILM " +
                "set NAME = ?, DESCRIPTION = ?, RELEASEDATE = ?, DURATION = ?, RATINGID = ? " +
                "where FILMID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId());

        return getFilm(film.getId());
    }

    @Override
    public boolean deleteFilm(Film film) {
        String sqlQuery = "delete from FILM where FILMID = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        return true;
    }

    private Film makeFilm(ResultSet resultSet) throws SQLException {
        int filmId = resultSet.getInt("FilmId");
        return new Film(
                filmId,
                resultSet.getNString("Name"),
                resultSet.getString("Description"),
                Objects.requireNonNull(resultSet.getDate("ReleaseDate")).toLocalDate(),
                resultSet.getLong("Duration")
        );
    }
}
