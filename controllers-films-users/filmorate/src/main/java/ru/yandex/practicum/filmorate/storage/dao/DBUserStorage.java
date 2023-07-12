package ru.yandex.practicum.filmorate.storage.dao;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;
import java.util.Collection;
import java.util.Objects;

@Component("DBUserStorage")
public class DBUserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public DBUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate=jdbcTemplate;
    }
    @Override
    public User getUser(Integer id) {
        String sqlUser = "select * from USERS where USERID = ?";
        User user;
        try {
            user = jdbcTemplate.queryForObject(sqlUser, (rs, rowNum) -> makeUser(rs), id);
        }
        catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь с идентификатором " +
                    id + " не зарегистрирован");
        }
        return  user;
    }
    @Override
    public Collection<User> getAllUsers(){
        String sqlAllUsers = "select * from USERS";
        return jdbcTemplate.query(sqlAllUsers, (rs, rowNum) -> makeUser(rs));
    }
    @Override
    public User addUser(User user) {
        String sqlQuery = "insert into USERS " +
                "(EMAIL,LOGIN, NAME, BIRTHDAY) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setDate(4, Date.valueOf(user.getBirthday()));

            return preparedStatement;
        }, keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();

        return getUser(id);
    }
    @Override
    public User updateUser(User user) {
        String sqlUser = "update USERS set " +
                "EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? " +
                "where USERID = ?";
        jdbcTemplate.update(sqlUser,
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());

        return  getUser(user.getId());
    }
    @Override
    public boolean deleteUser(User user) {
        String sqlQuery = "delete from USERS where USERID = ?";
        return jdbcTemplate.update(sqlQuery, user.getId()) >0;
    }
    private User makeUser(ResultSet resultSet) throws SQLException {
        int userId = resultSet.getInt("UserID");
        return new User(
                userId,
                resultSet.getString("Email"),
                resultSet.getString("Login"),
                resultSet.getString("Name"),
                Objects.requireNonNull(resultSet.getDate("BirthDay")).toLocalDate());
    }

}
