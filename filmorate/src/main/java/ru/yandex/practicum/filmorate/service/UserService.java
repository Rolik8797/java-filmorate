package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Map;

@Component
@Slf4j
@AllArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Map<Integer, User> getAllUsers(){
        return userStorage.getUsers();
    }
    public User createUser(User user){
        return userStorage.create(user);
    }
    public User updateUser(User user){
        return userStorage.update(user);
    }
    public void setUserNameByLogin(User user, String text){
        if (user.getName()==null || user.getName().isBlank()){
            user.setName(user.getLogin());
        }
        log.debug("{} пользователь: {}, email: {}", text, user.getName(), user.getEmail());
    }
}
