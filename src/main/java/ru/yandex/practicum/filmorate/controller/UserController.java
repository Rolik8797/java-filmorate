package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;


@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(@Autowired(required = false) UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен запрос GET к эндопоинту: /users");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findFriends(@PathVariable String id) {
        log.info("Получен запрос GET к эндопоинту: /users/{}/friends", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable String id) {
        log.info("Получен запрос GET к эндопоинту: /users/{}/", id);
        return userService.getUser(id);
    }


    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findCommonFriends(@PathVariable String id, @PathVariable String otherId) {
        log.info("Получен запрос GET к эндопоинту: /users/{}/friends/common/{}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        log.info("Received POST request. Request body: {}", user);

        try {
            User validUser = userService.add(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(validUser); // HTTP 201
        } catch (UserValidationException ex) {
            log.error("User validation error: {}", ex.getMessage());
            return ResponseEntity.badRequest().build(); // HTTP 400
        }
    }

    @PutMapping
    public ResponseEntity<User> put(@RequestBody User user) {
        log.info("Received PUT request. Request body: {}", user);

        try {
            User validUser = userService.update(user);
            return ResponseEntity.ok(validUser); // HTTP 200
        } catch (NotFoundException ex) {
            log.error("User with ID {} not found.", user.getId());
            return ResponseEntity.notFound().build(); // HTTP 404
        }
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable String id, @PathVariable String friendId) {
        log.info("Получен запрос PUT к эндопоинту: /users/{}/friends/{}", id, friendId);
        userService.addFriend(id, friendId);
        log.error("Обновлен объект {} с идентификатором {}. Добавлен друг {}",
                User.class.getSimpleName(), id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable String id, @PathVariable String friendId) {
        log.info("Получен запрос DELETE к эндопоинту: /users/{}/friends/{}", id, friendId);
        userService.deleteFriend(id, friendId);
        log.info("Обновлен объект {} с идентификатором {}. Удален друг {}",
                User.class.getSimpleName(), id, friendId);
    }
}