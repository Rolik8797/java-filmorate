package ru.yandex.practicum.filmorate.storage.user;


import ru.yandex.practicum.filmorate.model.User;

import java.util.Optional;
import java.util.List;

public interface UserStorage {
    User create(User user);

    User update(User user);

    List<User> getAllUsers();

    User getUserById(Integer id);

    List<User> getUserFriends(Integer userId);
}