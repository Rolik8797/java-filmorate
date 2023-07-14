package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getFilms() {
        List<User> usersList = new ArrayList<>(userService.getAllUsers().values());
        log.debug("Количество пользователей: {}", usersList.size());
        return usersList;
    }

    @PostMapping
    public User createFilm(@Valid @RequestBody User user) {
        if (userService.getAllUsers().containsKey(user.getId())) {
            throw new RuntimeException("Пользователь уже есть в базе");
        }
        userService.setUserNameByLogin(user, "Добавлен");
        return userService.createUser(user);
    }

    @PutMapping
    public User updateFilm(@RequestBody User user) {
        if (!userService.getAllUsers().containsKey(user.getId())) {
            throw new RuntimeException("Пользователь уже есть в базе");
        }
        userService.setUserNameByLogin(user, "Добавлен");
        return userService.createUser(user);
    }
}
