package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.annotation.CorrectReleaseDay;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film  {
    @PositiveOrZero(message = "id не может быть отрицательным")
    private int id;
    @NotBlank(message = "имя не должно быть пустым")
    private String name;
    @Length(min = 1, max = 200, message = "Длина описания должны быть от 1 до 200 символов")
    private String description;
    @CorrectReleaseDay(message = "дата релиза — не раньше 28 декабря 1895 года")
    private LocalDate releaseDate;
    @PositiveOrZero(message = "продолжительность фильма должна быть положительной")
    private Integer duration;

}
