package ru.yandex.practicum.filmorate.stroage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.DBGenreStorage;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DBGenreStorageTest {
    private final DBGenreStorage dbGenreStorage;

    @Test
    public void testGetGenreById() {
        Genre dbGenre = dbGenreStorage.getGenreById(1);
        assertThat(dbGenre).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void testGetAllGenres() {
        Collection<Genre> genres = dbGenreStorage.getAllGenres();
        assertEquals(6, genres.size());
    }
}
