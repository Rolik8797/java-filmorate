package ru.yandex.practicum.filmorate.stroage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.DBUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DBUserStorageTest {
    private final DBUserStorage dbUserStorage;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void beforeEach() {
        user1 = new User(1,
                "correct.email@mail.ru",
                "correct_login",
                "Correct Name",
                LocalDate.of(2002, 1, 1),
                new ArrayList<>());
        user2 = new User(2,
                "correct.email2@mail.ru",
                "correct_login2",
                "Correct Name",
                LocalDate.of(2002, 1, 1),
                new ArrayList<>());
        user3 = new User(3,
                "correct.email3@mail.ru",
                "correct_login3",
                "Correct Name",
                LocalDate.of(2002, 1, 1),
                new ArrayList<>());
    }

    @Test
    public void testGetUserById() {
        dbUserStorage.addUser(user1);
        User dbUser = dbUserStorage.getUser(1);
        assertThat(dbUser).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void getAllUsers() {
        dbUserStorage.addUser(user2);
        dbUserStorage.addUser(user3);
        Collection<User> dbUsers = dbUserStorage.getAllUsers();
        assertEquals(2, dbUsers.size());
    }

    @Test
    void deleteUser() {
        Collection<User> beforeDelete = dbUserStorage.getAllUsers();
        dbUserStorage.deleteUser(user1);
        Collection<User> afterDelete = dbUserStorage.getAllUsers();
        assertEquals(beforeDelete.size() - 1, afterDelete.size());
    }
}
