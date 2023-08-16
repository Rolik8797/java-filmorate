package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id;

    @Email(message = "Введеное значение не является адресом электронной почты. ")
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\S*$", message = "Логин не может содержать пробелы. ")
    private String login;
    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем. ")
    private LocalDate birthday;

    private List<Integer> friends;

    public boolean addFriend(final Integer id) {
        if (friends == null) {
            friends = new ArrayList<>();
        }
        return friends.add(id);
    }

    public boolean deleteFriend(final Integer id) {
        return friends.remove(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId();
    }
}
