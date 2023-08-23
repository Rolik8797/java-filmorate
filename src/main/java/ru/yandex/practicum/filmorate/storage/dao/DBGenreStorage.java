package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class DBGenreStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public DBGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean deleteFilmGenres(int filmId) {
        String deleteOldGenres = "delete from genreline where filmid = ?";
        jdbcTemplate.update(deleteOldGenres, filmId);
        return true;
    }

    @Override
    public boolean addFilmGenres(int filmId, Collection<Genre> genres) {
        String insertGenreLine = "insert into genreline (filmid, genreid) values (?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();

        for (Genre genre : genres) {
            String checkExisting = "select count(*) from genreline where filmid = ? and genreid = ?";
            int existingCount = jdbcTemplate.queryForObject(checkExisting, Integer.class, filmId, genre.getId());

            if (existingCount == 0) {
                batchArgs.add(new Object[]{filmId, genre.getId()});
            } else {
                System.out.println("Film ID: " + filmId + " with genre ID: " + genre.getId() + " already exists.");
            }
        }

        if (!batchArgs.isEmpty()) {
            String insertUniqueGenreLine = "insert into genreline (filmid, genreid) select ?, ? " +
                    "where not exists (select 1 from genreline where filmid = ? and genreid = ?)";
            List<Object[]> uniqueBatchArgs = new ArrayList<>();
            for (Object[] args : batchArgs) {
                uniqueBatchArgs.add(new Object[]{args[0], args[1], args[0], args[1]});
            }

            int[] updateCounts = jdbcTemplate.batchUpdate(insertUniqueGenreLine, uniqueBatchArgs);
            System.out.println("Added " + updateCounts.length + " genre lines.");
        }

        return true;
    }

    @Override
    public Collection<Genre> getGenresByFilmId(int filmId) {
        String sqlGenre = "select genre.genreid, name from genre " +
                "inner join genreline gl on genre.genreid = gl.genreid " +
                "where filmid = ?";
        return jdbcTemplate.query(sqlGenre, this::makeGenre, filmId);
    }

    @Override
    public Collection<Genre> getAllGenres() {
        String sqlGenre = "select genreid, name from genre order by genreid";
        return jdbcTemplate.query(sqlGenre, this::makeGenre);
    }

    @Override
    public Genre getGenreById(int genreId) {
        String sqlGenre = "select * from genre where genreid = ?";
        Genre genre;
        try {
            genre = jdbcTemplate.queryForObject(sqlGenre, this::makeGenre, genreId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанр с идентификатором " +
                    genreId + " не зарегистрирован!");
        }
        return genre;
    }

    private Genre makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("genreid"), resultSet.getString("name"));
    }
}