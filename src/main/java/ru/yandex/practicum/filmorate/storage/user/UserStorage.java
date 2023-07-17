package ru.yandex.practicum.filmorate.storage.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
@EqualsAndHashCode
@ToString
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
}
