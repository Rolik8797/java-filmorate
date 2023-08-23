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
import java.util.List;
import java.util.Objects;

@Component("DBUserStorage")
public class DBUserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public DBUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUser(Integer id) {
        String sqlUser = "select * from users where userid = ?";
        User user;
        try {
            user = jdbcTemplate.queryForObject(sqlUser, (rs, rowNum) -> makeUser(rs), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь с идентификатором " +
                    id + " не зарегистрирован!");
        }
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        String sqlAllUsers = "select * from users";
        return jdbcTemplate.query(sqlAllUsers, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User addUser(User user) {
        String sqlQuery = "insert into users " +
                "(email, login, name, birthday) " +
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
        String sqlUser = "update users set " +
                "email = ?, login = ?, name = ?, birthday = ? " +
                "where userid = ?";
        jdbcTemplate.update(sqlUser,
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());

        return getUser(user.getId());
    }

    @Override
    public boolean deleteUser(User user) {
        String sqlQuery = "delete from users where userid = ?";
        return jdbcTemplate.update(sqlQuery, user.getId()) > 0;
    }

    private User makeUser(ResultSet resultSet) throws SQLException {
        int userId = resultSet.getInt("userid");
        return new User(
                userId,
                resultSet.getString("email"),
                resultSet.getString("login"),
                resultSet.getString("name"),
                Objects.requireNonNull(resultSet.getDate("birthday")).toLocalDate(),
                getUserFriends(userId));
    }

    private List<Integer> getUserFriends(int userId) {
        String sqlGetFriends = "select friendid from friendship where userid = ?";
        return jdbcTemplate.queryForList(sqlGetFriends, Integer.class, userId);
    }

    @Override
    public boolean addFriend(int userId, int friendId) {
        try {
            String sqlGetFriendship = "SELECT status FROM friendship WHERE (userid = ? AND friendid = ?) OR (userid = ? AND friendid = ?)";
            Integer friendshipStatus = jdbcTemplate.queryForObject(sqlGetFriendship, Integer.class, userId, friendId, friendId, userId);

            String sqlAddFriendship = "INSERT INTO friendship (userid, friendid, status) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE status = ?";
            if (friendshipStatus == null) {
                jdbcTemplate.update(sqlAddFriendship, userId, friendId, false, false);
            } else if (friendshipStatus == 1) {
                jdbcTemplate.update(sqlAddFriendship, userId, friendId, true, true);
                jdbcTemplate.update(sqlAddFriendship, friendId, userId, true, true);
            } else {
                jdbcTemplate.update(sqlAddFriendship, userId, friendId, false, false);
            }
            return true;
        } catch (EmptyResultDataAccessException e) {
            String sqlAddFriendship = "INSERT INTO friendship (userid, friendid, status) VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlAddFriendship, userId, friendId, false);
            return true;
        }
    }

    @Override
    public boolean deleteFriend(int userId, int friendId) {
        String sqlDeleteFriend = "delete from friendship where userid = ? and friendid = ?";
        jdbcTemplate.update(sqlDeleteFriend, userId, friendId);
        String sqlSetStatus = "update friendship set status = false " +
                "where userid = ? and friendid = ?";
        jdbcTemplate.update(sqlSetStatus, friendId, userId);
        return true;
    }
}