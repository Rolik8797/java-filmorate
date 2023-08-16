package ru.yandex.practicum.filmorate.stroage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.DBMpaStorage;


import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DBMpaStorageTest {
    private final DBMpaStorage dbMpaStorage;

    @Test
    public void testGetMpaById() {
        Mpa dbMpa = dbMpaStorage.getMpaById(1);
        assertThat(dbMpa).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void testGetAllMpa() {
        Collection<Mpa> allMpa = dbMpaStorage.getAllMpa();
        assertEquals(5, allMpa.size());
    }
}
