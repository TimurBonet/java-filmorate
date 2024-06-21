package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDAO;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDAOImpl implements UserDAO {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAll() {
        String sqlQuery = "SELECT* FROM users ";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User findUserById(Integer id) {
        String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
        List<User> userList = jdbcTemplate.query(sqlQuery, this::mapRowToUser, id);
        if (userList.size() != 1) {
            throw new NotFoundException("Пользователь не найден", HttpStatus.NOT_FOUND);
        } else
            log.info("(UserDAOImpl->findUserById)Пользователь найденный по id: {} -> {}", userList.get(0).getId(), userList.get(0));
        return userList.get(0);
    }

    @Override
    public User createUser(User user) {
        String sqlQuery = "INSERT INTO users(email,login,name,birthday) " +
                "VALUES(?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getName());
            statement.setDate(4, Date.valueOf(user.getBirthday()));
            return statement;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        log.info("(UserDAOImpl->createUser)Created user: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users SET " +
                "email = ?, login = ?, name = ?, birthday = ? " +
                "WHERE user_id = ?";
        int update = jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        if (update > 0) {
            log.info("(UserDAOImpl->updateUser)Updated user: {}", user);
            return user;
        } else throw new NotFoundException("Пользоватеь не обновлён", HttpStatus.NOT_FOUND);
    }

    @Override
    public boolean addFriend(Integer id, Integer friendId) {
        String sqlQuery = "INSERT INTO friends_list(user_id, friend_id) " +
                "SELECT ?, ? " +
                "FROM DUAL " +
                "WHERE NOT EXISTS (SELECT 1 FROM " +
                "friends_list " +
                "WHERE (user_id = ? " +
                "AND friend_id = ?) " +
                "OR (user_id = ? " +
                "AND friend_id = ?))";
        //log.info("(UserDAOImpl->addFriend)Пользователь {}, имеет в друзьях {}, подтверждаем : {}", id, friendId, findUserById(id).getFriends());
        //log.info("(UserDAOImpl->addFriend)Пользователь {}, имеет в друзьях {}, подтверждаем : {}", friendId, id, findUserById(friendId).getFriends());

        return jdbcTemplate.update(sqlQuery, id, friendId, id, friendId, friendId, id) > 0;
    }

    @Override
    public boolean deleteFriend(Integer id, Integer friendId) {
        String sqlQuery = " DELETE FROM friends_list WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
        return true;
    }

    @Override
    public List<User> getFriendList(Integer id) {
        String sqlQuery = "SELECT users.user_id, email, login, name, birthday " +
                "FROM users " +
                "LEFT JOIN friends_list fl ON users.user_id = fl.friend_id " +
                "WHERE fl.user_id = ?";
        List<User> userList = jdbcTemplate.query(sqlQuery, this::mapRowToUser, id);
        log.info("(UserDAOImpl->getFriendList)Френдлист пользователя id {} -> {}", id, userList.stream()
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toList()));
        return userList.stream()
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        String sqlQuery = "SELECT u.user_id, email, login, name, birthday " +
                "FROM friends_list AS fl " +
                "LEFT JOIN users u ON u.user_id = fl.friend_id " +
                "WHERE fl.user_id = ? AND fl.friend_id IN ( " +
                "SELECT friend_id " +
                "FROM friends_list AS fl " +
                "LEFT JOIN users AS u ON u.user_id = fl.friend_id " +
                "WHERE fl.user_id = ?)";

        log.info("(UserDAOImpl->getCommonFriends)  : {}", jdbcTemplate.query(sqlQuery, this::mapRowToUser, id, otherId));
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, id, otherId);
    }

    @Override
    public boolean isExistById(Integer id) {
        String sqlQuery = "SELECT EXISTS(SELECT 1 FROM users WHERE user_id = ?)";
        return jdbcTemplate.queryForObject(sqlQuery, Boolean.class, id);
    }

    @Override
    public boolean isExist(Integer id) {
        String sqlQuery = "SELECT EXISTS(SELECT 1 FROM users WHERE user_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sqlQuery, Boolean.class, id));
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}
