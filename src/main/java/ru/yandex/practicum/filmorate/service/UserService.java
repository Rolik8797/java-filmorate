package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserFoundException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Integer userId) {
        return userStorage.getUserById(userId);
    }

    public User createUser(User user) {
        if (userStorage.getAllUsers().stream().anyMatch(u -> u.getId() == user.getId())) {
            throw new UserFoundException("Пользователь уже есть в базе");
        }
        setUserNameByLogin(user, "Добавлен");
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        if (userStorage.getAllUsers().stream().noneMatch(u -> u.getId() == user.getId())) {
            throw new FilmNotFoundException("Пользователя нет в базе");
        }
        setUserNameByLogin(user, "Обновлен");
        return userStorage.update(user);
    }

    public void addFriend(Integer userId, Integer friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
        log.debug("Пользователь с id {} добавил в список друзей с id {}", userId, friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        log.debug("Пользователь с id {} удален из списка друзей пользователем с id {}", userId, friendId);
    }

    public List<User> getUserFriends(Integer userId) {
        return userStorage.getUserFriends(userId);
    }

    public Set<User> getMutualFriends(Integer userId, Integer otherId) {
        return getUserById(userId).getFriends()
                .stream()
                .filter(getUserById(otherId).getFriends()::contains)
                .map(this::getUserById)
                .collect(Collectors.toSet());
    }

    public void setUserNameByLogin(User user, String text) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.debug("{} пользователь: {}, email: {}", text, user.getName(), user.getEmail());
    }
}