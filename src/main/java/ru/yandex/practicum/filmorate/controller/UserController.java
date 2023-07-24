package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        List<User> usersList = userService.getAllUsers();
        log.debug("Количество пользователей: {}", usersList.size());
        return usersList;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.debug("Создание пользователя: {}", user);
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.debug("Обновление пользователя: {}", user);
        return userService.updateUser(user);
    }
}