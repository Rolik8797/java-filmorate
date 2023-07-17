package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Map<Integer, User> getAllUsers() {
        return userStorage.getUsers();
    }

    public User createUser(User user) {
        if (userStorage.getUsers().containsKey(user.getId())) {
            throw new AlreadyExistsException("Пользователь уже есть в базе");
        }
        setUserNameByLogin(user, "Добавлен");
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        if (!userStorage.getUsers().containsKey(user.getId())) {
            throw new NotFoundException("Пользователя нет в базе");
        }
        setUserNameByLogin(user, "Обновлен");
        return userStorage.update(user);
    }

    public void setUserNameByLogin(User user, String text) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.debug("{} пользователь: {}, email: {}", text, user.getName(), user.getEmail());
    }
}
