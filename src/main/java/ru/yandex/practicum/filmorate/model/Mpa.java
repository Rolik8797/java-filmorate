package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
public class Mpa {
    @PositiveOrZero(message = "id can not be negative")
    private int id;

    @NotBlank(message = "name must not be empty")
    @NotNull(message = "name must not be null")
    private String name;
}
