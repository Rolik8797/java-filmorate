package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.CorrectLogin;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    @PositiveOrZero(message = "id не может быть отрицательным")
    private int id;
    @NotNull(message = "логин не может быть нулевым")
    @Email(message = "invalid email")
    private String email;
    @NotBlank(message = "логин не может быть пустым")
    @CorrectLogin
    private String login;

    private String name;

    @PastOrPresent
    private LocalDate birthday;
}
