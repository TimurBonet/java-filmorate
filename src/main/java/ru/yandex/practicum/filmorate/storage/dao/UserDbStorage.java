package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAll() {
        String sqlQuery = "SELECT* FROM USERS ";
        return jdbcTemplate.query(sqlQuery,this::mapRowToUser);
    }

    @Override
    public User findUserById(Integer id) {
        String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        List<User> userList = jdbcTemplate.query(sqlQuery,this::mapRowToUser,id);
        if (userList.size() != 1){
            throw new NotFoundException("Пользователь не найден", HttpStatus.NOT_FOUND);
        } else
            return userList.get(0);
    }

    @Override
    public User createUser(User user) {
        String sqlQuery = "INSERT INTO USERS(NAME,EMAIL,LOGIN,BIRTHDAY) " +
                "VALUES(?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            statement.setString(1,user.getName());
            statement.setString(2,user.getEmail());
            statement.setString(3,user.getLogin());
            statement.setDate(4, Date.valueOf(user.getBirthday()));
            return statement;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE USERS SET" +
                "NAME = ?, EMAIL = ?, LOGIN = ?, BIRTHDAY = ?" +
                "WHERE USER_ID = ?";
        int update = jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday(),
                user.getId()
                );
        if(update > 0) {
            return user;
        } else throw new NotFoundException("Пользоватеь не обновлён",HttpStatus.NOT_FOUND);
    }
    @Override
    public boolean addFriend(Integer id, Integer friendId) {
        String sqlQuery = "INSERT INTO FRIENDS_LIST(USER_ID, FRIEND_ID)" +
                "SELECT ?, ? " +
                "FROM DUAL " +
                "WHERE NOT EXISTS " +
                "FRIENDS_LIST " +
                "WHERE (USER_ID = ? " +
                "AND FRIEND_ID = ?) " +
                "OR (USER_ID = ? " +
                "AND FRIEND_ID = ?";
        return  jdbcTemplate.update(sqlQuery, id, friendId, id, id, friendId, friendId , id) > 0;
    }

    @Override
    public boolean deleteFriend(Integer id, Integer friendId) {
        String sqlQuery = " DELETE FROM FRIENDS_LIST WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
        return true;
    }

    @Override
    public List<User> getFriendList(Integer id) {
        String sqlQuery = "SELECT USERS.USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY " +
                "FROM USERS " +
                "LEFT JOIN FRIENDS_LIST fl ON USERS.USER_ID = fl.FRIEND_ID " +
                "WHERE f.USER_ID = ?";
        List<User> userList = jdbcTemplate.query(sqlQuery, this::mapRowToUser, id);
        return  userList.stream()
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        String sqlQuery = "SELECT u.USER_ID, EMAIL,LOGIN, NAME, BIRTHDAY " +
                "FROM FRIENDS_LIST AS f " +
                "LEFT JOIN USERS u ON u.USER_ID = f.FRIEND_ID " +
                "WHERE f.USER_ID = ? AMD f.FRIEND_ID IN ( " +
                "SELECT FRIEND_ID " +
                "FROM FIENDS_LIST AS fl " +
                "LEFT JOIN USERS AS u ON u.USER_ID = f.FRIEND_ID " +
                "WHERE f.USER_ID = ?";

        return  jdbcTemplate.query(sqlQuery,this::mapRowToUser,id,otherId);
    }

    @Override
    public boolean isExistById(Integer id) {
        String sqlQuery = "SELECT EXISTS(SELECT 1 FROM USERS WHERE USER_ID =?)";
        return jdbcTemplate.queryForObject(sqlQuery, Boolean.class, id);
    }

    @Override
    public boolean isExist(Integer id) {
        String sqlQuery = "SELECT EXISTS(SELECT 1 FROM USERS WHERE USER ID = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sqlQuery,Boolean.class,id));
    }

    private User mapRowToUser(ResultSet resultSet,int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("USER_ID"))
                .email(resultSet.getString("EMAIL"))
                .login(resultSet.getString("LOGIN"))
                .name(resultSet.getString("NAME"))
                .birthday(resultSet.getDate("BIRTHDAY").toLocalDate())
                .build();
    }
}
