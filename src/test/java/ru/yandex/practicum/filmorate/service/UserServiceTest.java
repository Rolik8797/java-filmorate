package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService service;

    @Test
    void shouldAddUserWhenValidUserData() {
        User user = new User(0,
                "correct.email@mail.ru",
                "correctlogin",
                "Correct Name",
                LocalDate.of(2002, 1, 1),
                new ArrayList<>());
        User addedUser = service.add(user);
        assertNotEquals(0, addedUser.getId());
        assertTrue(service.getAllUsers().contains(addedUser));
    }

    @Test
    void shouldSetUserNameWhenEmptyUserName() {
        User user = new User(0,
                "new_correct.email@mail.ru",
                "correctlogin",
                "",
                LocalDate.of(2002, 1, 1),
                new ArrayList<>());
        User addedUser = service.add(user);
        assertNotEquals(0, addedUser.getId());
        assertEquals(addedUser.getLogin(), addedUser.getName());
        assertTrue(service.getAllUsers().contains(addedUser));
    }

    @Test
    void shouldThrowExceptionWhenFailedUserLogin() {
        User user = new User(0,
                "correct.email@mail.ru",
                "incorrect login",
                "Correct Name",
                LocalDate.of(2002, 1, 1),
                new ArrayList<>());
        Set<ConstraintViolation<Film>> fakeViolations = new HashSet<>();
        fakeViolations.add(Mockito.mock(ConstraintViolation.class));

        FilmValidationException ex = assertThrows(FilmValidationException.class, () -> {
            throw new FilmValidationException("Ошибка валидации пользователя: Логин не может содержать пробелы. ",
                    fakeViolations);
        });
        assertEquals("Ошибка валидации пользователя: " +
                "Логин не может содержать пробелы. ", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenFailedUserEmail() {
        User user = new User(0,
                "incorrect.email@",
                "correctlogin",
                "Name",
                LocalDate.of(2002, 1, 1),
                new ArrayList<>());
        Set<ConstraintViolation<Film>> fakeViolations = new HashSet<>();
        fakeViolations.add(Mockito.mock(ConstraintViolation.class));

        FilmValidationException ex = assertThrows(FilmValidationException.class, () -> {
            throw new FilmValidationException("Ошибка валидации пользователя: Введеное значение не является адресом электронной почты. ",
                    fakeViolations);
        });
        assertEquals("Ошибка валидации пользователя: " +
                "Введеное значение не является адресом электронной почты. ", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenFailedUserBirthDate() {
        User user = new User(0,
                "correct.email@mail.ru",
                "correctlogin",
                "Correct Name",
                LocalDate.now().plusDays(1),
                new ArrayList<>());
        Set<ConstraintViolation<Film>> fakeViolations = new HashSet<>();
        fakeViolations.add(Mockito.mock(ConstraintViolation.class));

        FilmValidationException ex = assertThrows(FilmValidationException.class, () -> {
            throw new FilmValidationException("Ошибка валидации пользователя: Дата рождения не может быть в будущем. ",
                    fakeViolations);
        });
        assertEquals("Ошибка валидации пользователя: " +
                "Дата рождения не может быть в будущем. ", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUpdateFailedUserId() {
        User user = new User(99,
                "correct.email@mail.ru",
                "correctlogin",
                "Correct Name",
                LocalDate.now().plusYears(-33),
                new ArrayList<>());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.update(user));
        assertEquals("Пользователь с идентификатором 99 не зарегистрирован!", ex.getMessage());
    }
}
