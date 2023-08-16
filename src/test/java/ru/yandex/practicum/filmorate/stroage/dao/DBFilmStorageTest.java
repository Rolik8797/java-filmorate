package ru.yandex.practicum.filmorate.stroage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.DBFilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DBFilmStorageTest {
    private final DBFilmStorage filmStorage;

    @Test
    public void testGetFilmById() {
        Film createFilm = new Film(1,
                "first",
                "first description",
                LocalDate.now().minusYears(8),
                90L,
                3,
                new Mpa(1, "o", "о"),
                new ArrayList<>(),
                new ArrayList<>());
        filmStorage.addFilm(createFilm);

        Film dbFilm = filmStorage.getFilm(1);
        assertThat(dbFilm).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void testGetAllFilms() {
        Film first = new Film(1,
                "first",
                "first description",
                LocalDate.now().minusYears(8),
                90L,
                3,
                new Mpa(1, "o", "о"),
                new ArrayList<>(),
                new ArrayList<>());
        Film second = new Film(2,
                "second",
                "second description",
                LocalDate.now().minusYears(15),
                100L,
                2,
                new Mpa(3, "o", "о"),
                new ArrayList<>(),
                new ArrayList<>());
        filmStorage.addFilm(first);
        filmStorage.addFilm(second);

        Collection<Film> dbFilms = filmStorage.getAllFilms();
        assertEquals(2, dbFilms.size());
    }

    @Test
    void testUpdateFilm() {
        Film first = new Film(1,
                "first",
                "first description",
                LocalDate.now().minusYears(8),
                90L,
                3,
                new Mpa(1, "o", "о"),
                new ArrayList<>(),
                new ArrayList<>());
        Film added = filmStorage.addFilm(first);
        added.setName("update");
        filmStorage.updateFilm(added);
        Film dbFilm = filmStorage.getFilm(added.getId());
        assertThat(dbFilm).hasFieldOrPropertyWithValue("name", "update");
    }

    @Test
    void testDeleteFilm() {
        Film first = new Film(1,
                "first",
                "first description",
                LocalDate.now().minusYears(8),
                90L,
                3,
                new Mpa(1, "o", "о"),
                new ArrayList<>(),
                new ArrayList<>());
        Film second = new Film(0,
                "second",
                "second description",
                LocalDate.now().minusYears(15),
                100L,
                2,
                new Mpa(3, "o", "о"),
                new ArrayList<>(),
                new ArrayList<>());
        Film addedFirst = filmStorage.addFilm(first);
        Film addedSecond = filmStorage.addFilm(second);

        Collection<Film> beforeDelete = filmStorage.getAllFilms();
        filmStorage.deleteFilm(addedFirst);
        Collection<Film> afterDelete = filmStorage.getAllFilms();
        assertEquals(beforeDelete.size() - 1, afterDelete.size());
    }
}