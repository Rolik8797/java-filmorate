package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;


@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User getUser(final Integer id) {
        return users.get(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        Collection<User> allUsers = users.values();
        if (allUsers.isEmpty()) {
            allUsers.addAll(users.values());
        }
        return allUsers;
    }

    @Override
    public User addUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!getAllUsers().contains(user)) {
            throw new NotFoundException("Пользователь с идентификатором " +
                    user.getId() + " не зарегистрирован!");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean deleteUser(User user) {
        users.remove(user.getId());
        return true;
    }

    @Override
    public boolean addFriend(int userId, int friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
        updateUser(user);
        updateUser(friend);
        return true;
    }

    @Override
    public boolean deleteFriend(int userId, int friendId) {
        return false;
    }

    @Override
    public Collection<User> getUsersByIds(Collection<Integer> userIds) {
        return Collections.emptyList();
    }
}
