package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashSet;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен запрос GET к эндопоинту: /users");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findFriends(@PathVariable Integer id) {
        log.info("Получен запрос GET к эндопоинту: /users/{}/friends", id);
        User user = userService.getUser(id);
        Collection<User> friends = new HashSet<>();
        for (Integer friendId : user.getFriends()) {
            friends.add(userService.getUser(friendId));
        }
        return friends;
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable Integer id) {
        log.info("Получен запрос GET к эндопоинту: /users/{}/", id);
        return userService.getUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Получен запрос GET к эндопоинту: /users/{}/friends/common/{}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос POST. Данные тела запроса: {}", user);
        final User validUser = userService.add(user);
        log.info("Создан объект {} с идентификатором {}", User.class.getSimpleName(), validUser.getId());
        return validUser;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        log.info("Получен запрос PUT. Данные тела запроса: {}", user);
        final User validUser = userService.update(user);
        log.info("Обновлен объект {} с идентификатором {}", User.class.getSimpleName(), validUser.getId());
        return validUser;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Получен запрос PUT к эндопоинту: /users/{}/friends/{}", id, friendId);
        userService.addFriend(id, friendId);
        log.info("Обновлен объект {} с идентификатором {}. Добавлен друг {}",
                User.class.getSimpleName(), id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Получен запрос DELETE к эндопоинту: /users/{}/friends/{}", id, friendId);
        userService.deleteFriend(id, friendId);
        log.info("Обновлен объект {} с идентификатором {}. Удален друг {}",
                User.class.getSimpleName(), id, friendId);
    }

}