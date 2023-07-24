package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.CorrectLogin;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    @PositiveOrZero(message = "id can not be negative")
    private int id;

    @NotNull(message = "login must not be null")
    @Email(message = "invalid email")
    private String email;

    @NotBlank(message = "login must not be empty")
    @CorrectLogin
    private String login;

    private String name;

    @PastOrPresent
    private LocalDate birthday;
}
