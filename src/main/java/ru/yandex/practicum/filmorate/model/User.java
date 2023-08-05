package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.CorrectLogin;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Valid
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

    private Set<Integer> friends;

    public void addFriend(Integer id) {
        if (friends == null) {
            friends = new HashSet<>();
        }
        friends.add(id);
    }

    public Set<Integer> getFriendsId() {
        if (friends == null) {
            friends = new HashSet<>();
        }
        return friends;
    }
}
