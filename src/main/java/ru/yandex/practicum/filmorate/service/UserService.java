package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.exception.WrongIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;


import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Slf4j
@Service
public class UserService {
    private int increment = 0;
    private final Validator validator;
    private final UserStorage userStorage;

    @Autowired
    public UserService(Validator validator, @Qualifier("DBUserStorage") UserStorage userStorage) {
        this.validator = validator;
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUsers() {
        try {
            return userStorage.getAllUsers();
        } catch (Exception e) {
            // Обработка ошибки: возникла проблема при получении списка пользователей
            throw new RuntimeException("Error while retrieving user list", e);
        }
    }

    public User add(final User user) {
        validate(user);
        return userStorage.addUser(user);
    }

    public User update(final User user) {
        validate(user);
        User existingUser = userStorage.getUser(user.getId());
        if (existingUser == null) {
            throw new NotFoundException("Пользователя нет в базе");
        }
        return userStorage.updateUser(user);
    }

    public void addFriend(final String supposedUserId, final String supposedFriendId) {
        try {
            User user = getStoredUser(supposedUserId);
            User friend = getStoredUser(supposedFriendId);

            if (user.getFriends().contains(friend.getId())) {
                throw new UserValidationException("Этот друг уже добавлен в список друзей", null);
            }

            userStorage.addFriend(user.getId(), friend.getId());
        } catch (NotFoundException e) {
            throw new NotFoundException("User or friend not found");
        } catch (UserValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Произошла ошибка при добавлении друга: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", e);
        }
    }

    public void deleteFriend(final String supposedUserId, final String supposedFriendId) {
        User user = getStoredUser(supposedUserId);
        User friend = getStoredUser(supposedFriendId);

        if (user.getFriends().contains(friend.getId())) {
            userStorage.deleteFriend(user.getId(), friend.getId());
        } else {
            throw new NotFoundException("Друг не найден в списке друзей пользователя");
        }
    }

    public Collection<User> getFriends(final String supposedUserId) {
        try {
            User user = getStoredUser(supposedUserId);
            Collection<User> friends = new HashSet<>();
            for (Integer id : user.getFriends()) {
                User friend = userStorage.getUser(id);
                if (friend != null) {
                    friends.add(friend);
                } else {
                    // Обработка ошибки: друг не найден
                    throw new NotFoundException("Друг не найден");
                }
            }
            return friends;
        } catch (NotFoundException e) {
            // Обработка ошибки: пользователь не найден
            throw new NotFoundException("Пользователь не найден");
        }
    }

    public Collection<User> getCommonFriends(final String supposedUserId, final String supposedOtherId) {
        try {
            User user = getStoredUser(supposedUserId);
            User otherUser = getStoredUser(supposedOtherId);

            Collection<User> commonFriends = new HashSet<>();
            for (Integer id : user.getFriends()) {
                if (otherUser.getFriends().contains(id)) {
                    User commonFriend = userStorage.getUser(id);
                    if (commonFriend != null) {
                        commonFriends.add(commonFriend);
                    } else {
                        // Обработка ошибки: общий друг не найден
                        throw new NotFoundException("Общий друг не найден");
                    }
                }
            }
            return commonFriends;
        } catch (NotFoundException e) {
            // Обработка ошибки: пользователь не найден
            throw new NotFoundException("Пользователь не найден");
        }
    }

    public User getUser(final String supposedId) {
        try {
            return getStoredUser(supposedId);
        } catch (NotFoundException e) {
            log.warn("Пользователь не найден: {}", e.getMessage());
            throw e;
        }
    }

    private void validate(final User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
            log.info("UserService: Поле name не задано. Установлено значение {} из поля login", user.getLogin());
        } else if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("UserService: Поле name не содержит буквенных символов. " +
                    "Установлено значение {} из поля login", user.getLogin());
        }
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            StringBuilder messageBuilder = new StringBuilder();
            for (ConstraintViolation<User> userConstraintViolation : violations) {
                messageBuilder.append(userConstraintViolation.getMessage());
            }
            throw new UserValidationException("Ошибка валидации пользователя: " + messageBuilder, violations);
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
            throw new WrongIdException("Не удалось распознать идентификатор пользователя: " +
                    "значение " + supposedId);
        }
        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с идентификатором " +
                    userId + " не зарегистрирован!");
        }
        return user;
    }
}