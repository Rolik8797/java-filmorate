package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private static int id;

    public int generateId() {
        return ++id;
    }

    @Override
    public User create(User user) {
        if (users.containsKey(user.getId())) {
            throw new AlreadyExistsException(String.format("Пользователь с id=%d есть в базе", user.getId()));
        }
        int newUserId = generateId();
        user.setId(newUserId);
        users.put(newUserId, user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException(String.format("Пользователя с id=%d нет в базе", user.getId()));
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Integer id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException(String.format("Пользователь с id=%d не найден", id));
        }
        return users.get(id);
    }

    @Override
    public List<User> getUserFriends(Integer userId) {
        return users.get(userId).getFriends()
                .stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public boolean exists(Integer userId) {
        return users.containsKey(userId);
    }
}
