package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
        if (allUsers.isEmpty()){
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
            throw new NotFoundException("Пользователь с индифификатором " +
                    user.getId() + " не зарегистрирован");
        }
        users.put(user.getId(), user);
        return user;
    }
    @Override
    public boolean deleteUser(User user){
        users.remove(user.getId());
        return true;
    }
}
