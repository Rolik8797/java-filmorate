package ru.yandex.practicum.filmorate.model;


import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.annotations.CorrectReleaseDay;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
public class Film {
    @PositiveOrZero(message = "id can not be negative")
    private int id;

    @NotBlank(message = "name must not be empty")
    private String name;

    @Length(min = 1, max = 200, message = "description length must be between 1 and 200")
    private String description;

    @CorrectReleaseDay(message = "releaseDate must be after 28-DEC-1895")
    private LocalDate releaseDate;

    @PositiveOrZero(message = "duration can not be negative")
    private Integer duration;

    private Set<Integer> likes;

    public Film(String name, String description, LocalDate releaseDate, Integer duration, Set<Integer> likes) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = Objects.requireNonNullElseGet(likes, HashSet::new);
    }

    public void addLike(Integer id) {
        if (likes == null) {
            likes = new HashSet<>();
        }
        likes.add(id);
    }

    public void deleteLike(Integer id) {
        likes.remove(id);
    }
}
