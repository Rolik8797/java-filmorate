package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.exception.WrongIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;


import javax.validation.ConstraintViolation;
import javax.validation.Valid;
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
        return userStorage.getAllUsers();
    }

    public User add(@Valid final User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
            log.info("UserService: Поле name не задано. Установлено значение {} из поля login", user.getLogin());
        } else if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("UserService: Поле name не содержит буквенных символов. " +
                    "Установлено значение {} из поля login", user.getLogin());
        }

        return userStorage.addUser(user);
    }

    public User update(@Valid final User user) {

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            StringBuilder messageBuilder = new StringBuilder();
            for (ConstraintViolation<User> userConstraintViolation : violations) {
                messageBuilder.append(userConstraintViolation.getMessage());
            }
            throw new UserValidationException("Ошибка валидации пользователя: " + messageBuilder, violations);
        }
        return userStorage.updateUser(user);
    }

    public void addFriend(final Integer supposedUserId, final Integer supposedFriendId) {
        User user = getStoredUser(supposedUserId);
        User friend = getStoredUser(supposedFriendId);
        userStorage.addFriend(user.getId(), friend.getId());
    }

    public void deleteFriend(final Integer supposedUserId, final Integer supposedFriendId) {
        User user = getStoredUser(supposedUserId);
        User friend = getStoredUser(supposedFriendId);
        userStorage.deleteFriend(user.getId(), friend.getId());
    }

    public Collection<User> getCommonFriends(final Integer supposedUserId, final Integer supposedOtherId) {
        User user = getStoredUser(supposedUserId);
        User otherUser = getStoredUser(supposedOtherId);

        Set<Integer> commonFriendIds = getCommonFriendIds(user, otherUser);
        Collection<User> commonFriends = new HashSet<>();

        for (Integer id : commonFriendIds) {
            commonFriends.add(userStorage.getUser(id));
        }

        return commonFriends;
    }

    public User getUser(final Integer supposedId) {
        return getStoredUser(supposedId);
    }


    private Integer idFromString(final Integer supposedId) {
        try {
            return Integer.valueOf(supposedId);
        } catch (NumberFormatException exception) {
            return Integer.MIN_VALUE;
        }
    }

    private User getStoredUser(final Integer supposedId) {
        final int userId = idFromString(supposedId);
        if (userId == Integer.MIN_VALUE) {
            throw new WrongIdException("Не удалось распознать идентификатор пользователя: " +
                    "значение " + supposedId);
        }
        User user = userStorage.getUser(userId);

        return user;
    }

    private Set<Integer> getCommonFriendIds(User user1, User user2) {
        Set<Integer> commonFriendIds = new HashSet<>(user1.getFriends());
        commonFriendIds.retainAll(user2.getFriends());
        return commonFriendIds;
    }
}