package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(@Autowired(required = false)UserService userService) {
         this.userService = userService;
    }
    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен запрос GET к эндопоинту: /users");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable String id) {
        log.info("Получен запрос GET к эндопоинту: /users/{}/", id);
        return userService.getUser(id);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Получен запрос POST. Данные тела запроса: {}", user);
        final User validUser = userService.add(user);
        log.info("Создан объект {} с индификатором {} ", User.class.getSimpleName(), validUser.getId());
        return validUser;
    }
    @PutMapping
    public User put(@RequestBody User user){
        log.info("Получен запрос PUT. Данные тела запроса: {}", user);
        final User validUser = userService.update(user);
        log.info("Обновлен объект {} с индификатором {} ", User.class.getSimpleName(), validUser.getId());
        return validUser;
    }

}
