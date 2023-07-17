package ru.yandex.practicum.filmorate.storage.user;


import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private static int id;

    public int generateId() {
        return ++id;
    }

    public User create(User user) {
        int newTaskId = generateId();
        user.setId(newTaskId);
        users.put(newTaskId, user);
        return user;
    }

    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public Map<Integer, User> getUsers() {
        return users;
    }

    public static int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserStorage that = (UserStorage) o;
        return users.equals(that.users);
    }

    @Override
    public int hashCode() {
        return users.hashCode();
    }

    @Override
    public String toString() {
        return "UserStorage{" +
                "users=" + users +
                '}';
    }
}

