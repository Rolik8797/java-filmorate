package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.annotation.ReleaseDateValidation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Film  {
    private int id;
    @NotBlank(message = "Имя должно содержать буквенные символы. ")
    private String name;
    @Size(max = 200, message = "Описание фильма не должно превышать 200 символов. ")
    private String description;
    @PastOrPresent(message = "Дата релиза не может быть в будущем. ")
    @ReleaseDateValidation
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма не может быть отрицательной. ")
    private long duration;


    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;
        Film film =(Film) o;
        return getId() == film.getId();
    }

    @Override
    public int hashCode () {
        return Objects.hash(getId());
    }
}
