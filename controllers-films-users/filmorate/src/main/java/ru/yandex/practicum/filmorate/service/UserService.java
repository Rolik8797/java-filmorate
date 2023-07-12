package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.exception.WrongIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Collection;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    private int increment = 0;
    private final Validator validator;

    private final UserStorage userStorage;
    @Autowired
    public  UserService(Validator validator, @Qualifier("DBUserStorage") UserStorage userStorage){
        this.validator = validator;
        this.userStorage = userStorage;
    }
    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }
    public User add(final  User user){
        validate(user);
        return userStorage.addUser(user);
    }
    public User update(final User user) {
        validate(user);
        return userStorage.updateUser(user);
    }
    public User getUser(final String supposedId) {
        return getStoredUser(supposedId);
    }
    private void validate (final User user) {

        if (user.getName() == null) {
            user.setName(user.getLogin());
            log.info("UserService: поле name не задано. Установлено значение {} из поля login", user.getLogin());
        } else if (user.getName().isEmpty() || user.getName().isBlank()){
            user.setName(user.getLogin());
            log.info("UserService: поле name не содержит буквенных символов. " +
                    "Установленно значение {} из поля login", user.getLogin());
        }
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            StringBuilder messageBuilder = new StringBuilder();
            for (ConstraintViolation<User>userConstraintViolation : violations) {
                messageBuilder.append(userConstraintViolation.getMessage());
            }
            throw new UserValidationException("Ошибка валидациипользователя: " + messageBuilder, violations);
        }
        if (user.getId() == 0) {
            user.setId(++increment);
        }
    }
    private Integer idFromString(final String supposedId) {
        try {
            return Integer.valueOf(supposedId);
        } catch (NumberFormatException exception) {
            return Integer.MIN_VALUE;
        }
    }
    private User getStoredUser(final String supposedId) {
        final int userId = idFromString(supposedId);
        if (userId == Integer.MIN_VALUE) {
            throw new WrongIdException("Не удалось распознать иденитификаторпользователя: " +
                    "значение " + supposedId);
        }
        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с идентификатором " +
                    userId + " не зарегистрирован");
        }
        return user;
    }
}
