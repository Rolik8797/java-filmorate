package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.annotations.ReleaseDateValidation;


import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    private int id;
    @NotBlank(message = "Имя должно содержать буквенныее символы. ")
    private String name;

    @Size(max = 200, message = "Описание фильма не должно превышать 200 символов. ")
    private String description;

    @PastOrPresent(message = "Дата релиза не может быть в будущем. ")
    @ReleaseDateValidation
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма не может быть отрицательной. ")
    private long duration;
    private int rate;

    @NotNull
    private Mpa mpa;
    private List<Genre> genres = new ArrayList<>();
    private List<Integer> likes = new ArrayList<>();

    public boolean addLike(Integer userId) {
        if (likes == null) {
            likes = new ArrayList<>();
        }
        return likes.add(userId);
    }

    public boolean deleteLike(Integer userId) {

        return likes.remove(userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;
        Film film = (Film) o;
        return getId() == film.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
