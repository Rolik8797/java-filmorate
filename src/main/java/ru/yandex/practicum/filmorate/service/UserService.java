package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
        if (userStorage.exists(user.getId())) {
            throw new AlreadyExistsException("Пользователь уже есть в базе");
        }
        setUserNameByLogin(user, "Добавлен");
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        if (userStorage.getAllUsers().stream().noneMatch(u -> u.getId() == user.getId())) {
            throw new NotFoundException("Пользователя нет в базе");
        }
        setUserNameByLogin(user, "Обновлен");
        return userStorage.update(user);
    }

    public List<User> getUserFriends(Integer userId) {
        return userStorage.getUserFriends(userId);
    }

    public Set<User> getMutualFriends(Integer userId, Integer otherId) {
        Set<Integer> userFriends = getUserById(userId).getFriendsId();
        Set<Integer> otherFriends = getUserById(otherId).getFriendsId();

        return userFriends.stream()
                .filter(otherFriends::contains)
                .map(this::getUserById)
                .collect(Collectors.toSet());
    }

    private void setUserNameByLogin(User user, String text) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.debug("{} пользователь: {}, email: {}", text, user.getName(), user.getEmail());
    }

    public void addFriend(Integer userId, Integer friendId) {
        addFriendInternal(userId, friendId);
        addFriendInternal(friendId, userId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        deleteFriendInternal(userId, friendId);
        deleteFriendInternal(friendId, userId);
    }

    private void addFriendInternal(Integer userId, Integer friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.addFriend(friendId);
        log.debug("Пользователь с id {} добавил в список друзей пользователя с id {}", userId, friendId);
    }

    private void deleteFriendInternal(Integer userId, Integer friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().remove(friendId);
        log.debug("Пользователь с id {} удален из списка друзей пользователя с id {}", userId, friendId);
    }
}