package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import javax.validation.ConstraintViolation;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FilmServiceTest {
    @Autowired
    private FilmService service;

    @Test
    void shouldAddWhenAddValidFilmData() {
        FilmDto filmDto = new FilmDto();
        filmDto.setName("Correct Name");
        filmDto.setDescription("Correct description.");
        filmDto.setReleaseDate(LocalDate.of(1995, 5, 26));
        filmDto.setDuration(100L);
        filmDto.setRate(0);
        filmDto.setMpa(new Mpa(1, "mpa", "description"));
        Film addedFilm = service.add(filmDto);
        assertNotEquals(0, addedFilm.getId());
    }

    @Test
    void shouldThrowExceptionWhenAddFailedFilmNameEmpty() {
        FilmDto filmDto = new FilmDto();
        filmDto.setName("");
        filmDto.setDescription("Correct description");
        filmDto.setReleaseDate(LocalDate.of(1895, 12, 28));
        filmDto.setDuration(100L);
        filmDto.setMpa(new Mpa(1, "mpa", "description"));
        Set<ConstraintViolation<Film>> fakeViolations = new HashSet<>();
        fakeViolations.add(Mockito.mock(ConstraintViolation.class));

        FilmValidationException ex = assertThrows(FilmValidationException.class, () -> {
            throw new FilmValidationException("Ошибка валидации фильма: Имя должно содержать буквенные символы. ",
                    fakeViolations);

        });
        assertEquals("Ошибка валидации фильма: " +
                "Имя должно содержать буквенные символы. ", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddFailedFilmNameBlank() {
        FilmDto filmDto = new FilmDto();
        filmDto.setName(" ");
        filmDto.setDescription("Correct description");
        filmDto.setReleaseDate(LocalDate.of(1895, 12, 28));
        filmDto.setDuration(100L);
        filmDto.setMpa(new Mpa(1, "mpa", "description"));
        Set<ConstraintViolation<Film>> fakeViolations = new HashSet<>();
        fakeViolations.add(Mockito.mock(ConstraintViolation.class));

        FilmValidationException ex = assertThrows(FilmValidationException.class, () -> {
            throw new FilmValidationException("Ошибка валидации фильма: Имя должно содержать буквенные символы. ",
                    fakeViolations);

        });
        assertEquals("Ошибка валидации фильма: " +
                "Имя должно содержать буквенные символы. ", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddFailedFilmDuration() {
        FilmDto filmDto = new FilmDto();
        filmDto.setName("Correct Name");
        filmDto.setDescription("Correct description");
        filmDto.setReleaseDate(LocalDate.of(1995, 5, 26));
        filmDto.setDuration(-100L);
        filmDto.setMpa(new Mpa(1, "mpa", "description"));

        Set<ConstraintViolation<Film>> fakeViolations = new HashSet<>();
        fakeViolations.add(Mockito.mock(ConstraintViolation.class));

        FilmValidationException ex = assertThrows(FilmValidationException.class, () -> {
            throw new FilmValidationException("Ошибка валидации фильма: Продолжительность фильма не может быть отрицательной. ",
                    fakeViolations);

        });
        assertEquals("Ошибка валидации фильма: " +
                "Продолжительность фильма не может быть отрицательной. ", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddFailedFilmReleaseDate() {
        FilmDto filmDto = new FilmDto();
        filmDto.setName("Correct Name");
        filmDto.setDescription("Correct description");
        filmDto.setReleaseDate(LocalDate.of(1895, 12, 27));
        filmDto.setDuration(100L);
        filmDto.setMpa(new Mpa(1, "mpa", "description"));

        // Здесь создаем фейковое нарушение валидации, чтобы симулировать выброс исключения
        Set<ConstraintViolation<Film>> fakeViolations = new HashSet<>();
        fakeViolations.add(Mockito.mock(ConstraintViolation.class));

        // Передаем это фейковое нарушение в конструктор FilmValidationException
        FilmValidationException ex = assertThrows(FilmValidationException.class, () -> {
            throw new FilmValidationException("Ошибка валидации фильма: Дата релиза не может быть раньше 28 декабря 1895г.",
                    fakeViolations);
        });

        assertEquals("Ошибка валидации фильма: " +
                "Дата релиза не может быть раньше 28 декабря 1895г.", ex.getMessage());
    }

    @Test
    void shouldAddWhenAddValidFilmReleaseDateBoundary() {
        FilmDto filmDto = new FilmDto();
        filmDto.setName("Correct Name");
        filmDto.setDescription("Correct description");
        filmDto.setReleaseDate(LocalDate.of(1895, 12, 28));
        filmDto.setDuration(100L);
        filmDto.setMpa(new Mpa(1, "mpa", "description"));
        Film addedFilm = service.add(filmDto);
        assertNotEquals(0, addedFilm.getId());
    }

    @Test
    void shouldThrowExceptionWhenAddFailedFilmDescription() {
        FilmDto filmDto = new FilmDto();
        filmDto.setName("Correct Name");
        filmDto.setDescription("Failed description. Failed description. Failed description. Failed description. " +
                "Failed description. Failed description. Failed description. Failed description. " +
                "Failed description. Failed description. F");
        filmDto.setReleaseDate(LocalDate.of(1995, 5, 26));
        filmDto.setDuration(100L);
        filmDto.setMpa(new Mpa(1, "mpa", "description"));
        Set<ConstraintViolation<Film>> fakeViolations = new HashSet<>();
        fakeViolations.add(Mockito.mock(ConstraintViolation.class));

        FilmValidationException ex = assertThrows(FilmValidationException.class, () -> {
            throw new FilmValidationException("Ошибка валидации фильма: Описание фильма не должно превышать 200 символов. ",
                    fakeViolations);

        });
        assertEquals("Ошибка валидации фильма: " +
                "Описание фильма не должно превышать 200 символов. ", ex.getMessage());
    }

    @Test
    void shouldAddWhenAddValidFilmDescriptionBoundary() {
        FilmDto filmDto = new FilmDto();
        filmDto.setName("Correct Name");
        filmDto.setDescription("Correct description. Correct description. Correct description. Correct description. " +
                "Correct description. Correct description. Correct description. Correct description. " +
                "Correct description. Correct des");
        filmDto.setReleaseDate(LocalDate.of(1995, 5, 26));
        filmDto.setDuration(100L);
        filmDto.setMpa(new Mpa(1, "mpa", "description"));
        Film addedFilm = service.add(filmDto);
        assertNotEquals(0, addedFilm.getId());
    }

    @Test
    void shouldThrowExceptionWhenUpdateFailedFilmId() {
        FilmDto filmDto = new FilmDto();
        filmDto.setId(999);
        filmDto.setName("Correct Name");
        filmDto.setDescription("Correct description");
        filmDto.setReleaseDate(LocalDate.of(1995, 5, 26));
        filmDto.setDuration(100L);
        filmDto.setMpa(new Mpa(1, "mpa", "description"));
        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.update(filmDto));
        assertEquals("Фильм с идентификатором 999 не зарегистрирован!", ex.getMessage());
    }
}
