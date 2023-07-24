package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserStorage {
    private final List<User> users = new ArrayList<>();
    private static int id;

    public int generateId() {
        return ++id;
    }

    public User create(User user) {
        int newUserId = generateId();
        user.setId(newUserId);
        users.add(user);
        return user;
    }

    public User update(User user) {
        int userIdToUpdate = user.getId();
        for (int i = 0; i < users.size(); i++) {
            User storedUser = users.get(i);
            if (storedUser.getId() == userIdToUpdate) {
                users.set(i, user);
                return user;
            }
        }
        throw new IllegalArgumentException("User with ID " + userIdToUpdate + " not found.");
    }

    public List<User> getUsers() {
        return users;
    }

    private int getId() {
        return id;
    }
}