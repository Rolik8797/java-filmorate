package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FilmServiceTest {
    @Autowired
    private FilmService service;

    @Test
    void shouldAddWhenAddValidFilmData() {
        Film film = new Film();
        film.setName("Correct Name");
        film.setDescription("Correct description.");
        film.setReleaseDate(LocalDate.of(1995, 5, 26));
        film.setDuration(100L);
        film.setRate(0);
        film.setMpa(new Mpa(1, "mpa", "description"));
        Film addedFilm = service.add(film);
        assertNotEquals(0, addedFilm.getId());
    }

    @Test
    void shouldThrowExceptionWhenAddFailedFilmNameEmpty() {
        Film film = new Film();
        film.setName("");
        film.setDescription("Correct description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100L);
        film.setMpa(new Mpa(1, "mpa", "description"));
        FilmValidationException ex = assertThrows(FilmValidationException.class, () -> service.add(film));
        assertEquals("Ошибка валидации фильма: " +
                "Имя должно содержать буквенные символы. ", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddFailedFilmNameBlank() {
        Film film = new Film();
        film.setName(" ");
        film.setDescription("Correct description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100L);
        film.setMpa(new Mpa(1, "mpa", "description"));
        FilmValidationException ex = assertThrows(FilmValidationException.class, () -> service.add(film));
        assertEquals("Ошибка валидации фильма: " +
                "Имя должно содержать буквенные символы. ", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddFailedFilmDuration() {
        Film film = new Film();
        film.setName("Correct Name");
        film.setDescription("Correct description");
        film.setReleaseDate(LocalDate.of(1995, 5, 26));
        film.setDuration(-100L);
        film.setMpa(new Mpa(1, "mpa", "description"));
        FilmValidationException ex = assertThrows(FilmValidationException.class, () -> service.add(film));
        assertEquals("Ошибка валидации фильма: " +
                "Продолжительность фильма не может быть отрицательной. ", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddFailedFilmReleaseDate() {
        Film film = new Film();
        film.setName("Correct Name");
        film.setDescription("Correct description");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(100L);
        film.setMpa(new Mpa(1, "mpa", "description"));
        FilmValidationException ex = assertThrows(FilmValidationException.class, () -> service.add(film));
        assertEquals("Ошибка валидации фильма: " +
                "Дата релиза не может быть раньше 28 декабря 1895г. ", ex.getMessage());
    }

    @Test
    void shouldAddWhenAddValidFilmReleaseDateBoundary() {
        Film film = new Film();
        film.setName("Correct Name");
        film.setDescription("Correct description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100L);
        film.setMpa(new Mpa(1, "mpa", "description"));
        Film addedFilm = service.add(film);
        assertNotEquals(0, addedFilm.getId());
    }

    @Test
    void shouldThrowExceptionWhenAddFailedFilmDescription() {
        Film film = new Film();
        film.setName("Correct Name");
        film.setDescription("Failed description. Failed description. Failed description. Failed description. " +
                "Failed description. Failed description. Failed description. Failed description. " +
                "Failed description. Failed description. F");
        film.setReleaseDate(LocalDate.of(1995, 5, 26));
        film.setDuration(100L);
        film.setMpa(new Mpa(1, "mpa", "description"));
        FilmValidationException ex = assertThrows(FilmValidationException.class, () -> service.add(film));
        assertEquals("Ошибка валидации фильма: " +
                "Описание фильма не должно превышать 200 символов. ", ex.getMessage());
    }

    @Test
    void shouldAddWhenAddValidFilmDescriptionBoundary() {
        Film film = new Film();
        film.setName("Correct Name");
        film.setDescription("Correct description. Correct description. Correct description. Correct description. " +
                "Correct description. Correct description. Correct description. Correct description. " +
                "Correct description. Correct des");
        film.setReleaseDate(LocalDate.of(1995, 5, 26));
        film.setDuration(100L);
        film.setMpa(new Mpa(1, "mpa", "description"));
        Film addedFilm = service.add(film);
        assertNotEquals(0, addedFilm.getId());
    }

    @Test
    void shouldThrowExceptionWhenUpdateFailedFilmId() {
        Film film = new Film();
        film.setId(999);
        film.setName("Correct Name");
        film.setDescription("Correct description");
        film.setReleaseDate(LocalDate.of(1995, 5, 26));
        film.setDuration(100L);
        film.setMpa(new Mpa(1, "mpa", "description"));
        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.add(film));
        assertEquals("Фильм с идентификатором 999 не зарегистрирован!", ex.getMessage());
    }
}
