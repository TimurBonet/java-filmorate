package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.storage.dao.UserDAOImpl;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {UserDAOImpl.class})
@ComponentScan(basePackages = {"ru.yandex.practicum.filmorate.dao"})
public class UserDAOImplTest {
    private final UserDAOImpl userDAOstorage;

    @Test
    void createUserTestOk() {
        final User user = generateUser("user1@yandex.ru", "user1", "User1 Name", LocalDate.of(1990, 03, 24));

        userDAOstorage.createUser(user);

        final List<User> users = userDAOstorage.findAll();

        assertEquals(1, users.size());
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("id", 3);
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("email", "user1@yandex.ru");
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("login", "user1");
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("name", "User1 Name");
        assertThat(users.get(0)).hasFieldOrProperty("birthday");
    }

    @Test
    void updateUserTestOk() {
        final User oldUser = generateUser("user1@yandex.ru", "user1", "User1 Name", LocalDate.of(1990, 03, 24));
        userDAOstorage.createUser(oldUser);
        final User user = generateUser("user3@yandex.ru", "user3", "User3 Name", LocalDate.of(1990, 03, 26));
        user.setId(4);

        userDAOstorage.updateUser(user);

        final User updatedUser = userDAOstorage.findUserById(4);

        assertThat(updatedUser).hasFieldOrPropertyWithValue("id", 4);
        assertThat(updatedUser).hasFieldOrPropertyWithValue("email", "user3@yandex.ru");
        assertThat(updatedUser).hasFieldOrPropertyWithValue("login", "user3");
        assertThat(updatedUser).hasFieldOrPropertyWithValue("name", "User3 Name");
        assertThat(updatedUser).hasFieldOrProperty("birthday");
    }

    @Test
    void getUserByIdTest() {
        final User currentUser = generateUser("user1@yandex.ru", "user1", "User1 Name", LocalDate.of(1990, 03, 24));
        userDAOstorage.createUser(currentUser);
        User user = userDAOstorage.findUserById(5);

        assertThat(user).hasFieldOrPropertyWithValue("id", 5);
        assertThat(user).hasFieldOrPropertyWithValue("email", "user1@yandex.ru");
        assertThat(user).hasFieldOrPropertyWithValue("login", "user1");
        assertThat(user).hasFieldOrPropertyWithValue("name", "User1 Name");
        assertThat(user).hasFieldOrProperty("birthday");
    }

    @Test
    void getAllUsersTest() {
        final User user1 = generateUser("user1@yandex.ru", "user1", "User1 Name", LocalDate.of(1990, 03, 24));
        userDAOstorage.createUser(user1);
        final User user2 = generateUser("user2@yandex.ru", "user2", "User2 Name", LocalDate.of(1990, 02, 25));
        userDAOstorage.createUser(user2);
        List<User> users = userDAOstorage.findAll();

        assertEquals(2, users.size());
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("id", 1);
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("email", "user1@yandex.ru");
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("login", "user1");
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("name", "User1 Name");
        assertThat(users.get(0)).hasFieldOrProperty("birthday");

        assertThat(users.get(1)).hasFieldOrPropertyWithValue("id", 2);
        assertThat(users.get(1)).hasFieldOrPropertyWithValue("email", "user2@yandex.ru");
        assertThat(users.get(1)).hasFieldOrPropertyWithValue("login", "user2");
        assertThat(users.get(1)).hasFieldOrPropertyWithValue("name", "User2 Name");
        assertThat(users.get(1)).hasFieldOrProperty("birthday");
    }


    private User generateUser(
            String email,
            String login,
            String name,
            LocalDate birthday) {
        return User.builder()
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
    }
}
