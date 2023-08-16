package ru.yandex.practicum.filmorate.storage.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component("DBFilmStorage")
public class DBFilmStorage implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(DBFilmStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private final GenreService genreService;

    public DBFilmStorage(JdbcTemplate jdbcTemplate, GenreService genreService) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreService = genreService;
    }

    @Override
    public Film getFilm(int filmId) {
        String sqlFilm = "select * from film " +
                "inner join ratingmpa r on film.ratingid = r.ratingid " +
                "where filmid = ?";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlFilm, (rs, rowNum) -> makeFilm(rs), filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильм с идентификатором " +
                    filmId + " не зарегистрирован!");
        }
        log.info("Найден фильм: {} {}", film.getId(), film.getName());
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sql = "select * from film " +
                "inner join ratingmpa r on film.ratingid = r.ratingid ";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> makeFilm(resultSet));
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "insert into film " +
                "(name, description, releasedate, duration, rate, ratingid) " +
                "values (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, film.getName());
            preparedStatement.setString(2, film.getDescription());
            preparedStatement.setDate(3, Date.valueOf(film.getReleaseDate()));
            preparedStatement.setLong(4, film.getDuration());
            preparedStatement.setInt(5, film.getRate());
            preparedStatement.setInt(6, Math.toIntExact(film.getMpa().getId()));
            return preparedStatement;
        }, keyHolder);

        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();

        if (!film.getGenres().isEmpty()) {
            genreService.addFilmGenres(film.getId(), film.getGenres());
        }
        if (film.getLikes() != null) {
            for (Integer userId : film.getLikes()) {
                addLike(film.getId(), userId);
            }
        }
        return getFilm(id);
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "update film " +
                "set name = ?, description = ?, releasedate = ?, duration = ?, rate = ?, ratingid = ? " +
                "where filmid = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());
        genreService.deleteFilmGenres(film.getId());
        if (!film.getGenres().isEmpty()) {
            genreService.addFilmGenres(film.getId(), film.getGenres());
        }
        if (film.getLikes() != null) {
            for (Integer userId : film.getLikes()) {
                addLike(film.getId(), userId);
            }
        }
        return getFilm(film.getId());
    }

    @Override
    public boolean deleteFilm(Film film) {
        String sqlQuery = "delete from film where filmid = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        return true;
    }

    @Override
    public boolean addLike(int filmId, int userId) {
        String sql = "select * from likes where userid = ? and filmid = ?";
        SqlRowSet existLike = jdbcTemplate.queryForRowSet(sql, userId, filmId);
        if (!existLike.next()) {
            String setLike = "insert into likes (userid, filmid) values (?, ?)";
            jdbcTemplate.update(setLike, userId, filmId);
        }
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, userId, filmId);
        log.info(String.valueOf(sqlRowSet.next()));
        return sqlRowSet.next();
    }

    @Override
    public boolean deleteLike(int filmId, int userId) {
        String deleteLike = "delete from likes where filmid = ? and userid = ?";
        jdbcTemplate.update(deleteLike, filmId, userId);
        return true;
    }

    @Override
    public Collection<Film> getMostPopularFilms(int count) {
        String sqlMostPopular = "select count(l.likeid) as likerate" +
                ", film.filmid" +
                ", film.name ,film.description ,releasedate ,duration ,rate ,r.ratingid, r.name, r.description from film " +
                "left join likes l on l.filmid = film.filmid " +
                "inner join ratingmpa r on r.ratingid = film.ratingid " +
                "group by film.filmid " +
                "order by likerate desc " +
                "limit ?";
        return jdbcTemplate.query(sqlMostPopular, (rs, rowNum) -> makeFilm(rs), count);
    }

    private Film makeFilm(ResultSet resultSet) throws SQLException {
        int filmId = resultSet.getInt("filmid");
        return new Film(
                filmId,
                resultSet.getString("name"),
                resultSet.getString("description"),
                Objects.requireNonNull(resultSet.getDate("releasedate")).toLocalDate(),
                resultSet.getLong("duration"),
                resultSet.getInt("rate"),
                new Mpa(resultSet.getInt("ratingmpa.ratingid"),
                        resultSet.getString("ratingmpa.name"),
                        resultSet.getString("ratingmpa.description")),
                (List<Genre>) genreService.getFilmGenres(filmId),
                getFilmLikes(filmId));
    }

    private List<Integer> getFilmLikes(int filmId) {
        String sqlGetLikes = "select userid from likes where filmid = ?";
        return jdbcTemplate.queryForList(sqlGetLikes, Integer.class, filmId);
    }
}