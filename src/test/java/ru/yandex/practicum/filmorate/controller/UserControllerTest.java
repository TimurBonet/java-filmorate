package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.InvalidEmailException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController = new UserController();
    List<User> usersList = new ArrayList<>();

    @BeforeEach
    public void beforeEach() {
        User user1 = new User("abc1@mail.ru", "cba", LocalDate.parse("1993-04-15"));
        user1.setName("Kirill");
        User user2 = new User("abc2@mail.ru", "NoName", LocalDate.parse("1994-05-16"));
        User user3 = new User("", "NoCorrectMail_1", LocalDate.parse("1994-05-16"));
        User user4 = new User("abc2mail.ru", "NoCorrectMail_2", LocalDate.parse("1994-05-16"));
        User user5 = new User("abc3@mail.ru", "futureBirthDay", LocalDate.parse("2345-05-16"));

        usersList.add(user1);
        usersList.add(user2);
        usersList.add(user3);
        usersList.add(user4);
        usersList.add(user5);
    }

    @Test
    void shouldFindAllUsers() {
        userController.createUser(usersList.get(0));
        List<User> allUsers = userController.findAll();
        assertEquals(1, allUsers.size(), "Количество пользователей в списке не соответствует");
        assertEquals(usersList.get(0), allUsers.get(0), "Не соответствуют пользователи");
    }

    @Test
    void shouldReturnErrorWhithBlankMail() {
        User user = usersList.get(2);
        assertEquals("", user.getEmail(), "Почта не пуста");
        Assertions.assertThrows(InvalidEmailException.class, () -> userController.createUser(user),
                "Не выбрасывает исключения InvalidEmailException при пустом email");
    }

    @Test
    void shouldReturnErrorWhithUncorrectMail() {
        User user = usersList.get(3);
        assertEquals(true, !(user.getEmail().contains("@")), "Почта имеет знак '@'");
        Assertions.assertThrows(InvalidEmailException.class, () -> userController.createUser(user),
                "Не выбрасывает исключения InvalidEmailException при email не содержащем '@'");
    }

    @Test
    void shouldReturnErrorWhithUncorrectBirthday() {
        User user = usersList.get(4);
        assertEquals(true, user.getBirthday().isAfter(LocalDate.now()), "Дата рождения не в будущем");
        Assertions.assertThrows(InvalidEmailException.class, () -> userController.createUser(user),
                "Не выбрасывает исключения InvalidEmailException при дате рождения в будущем");
    }

    @Test
    void shouldSetNameLikeLoginIfNameBlank() {
        User user = usersList.get(1);
        userController.createUser(user);
        assertEquals(user, userController.findAll().get(0), "Пользователь не совпадает");
        assertEquals("NoName", userController.findAll().get(0).getName(), "Присвоенное имя не совпадает");
    }

    @Test
    void createUser() {
        User user = usersList.get(0);
        userController.createUser(user);
        assertEquals(user, userController.findAll().get(0), "Пользователь не совпадает");
        assertEquals(1, userController.findAll().size(), "Количество пользователей не совпадает");
    }

    @Test
    void updateUser() {
        User user = usersList.get(0);
        userController.createUser(user);
        User user2 = userController.findAll().get(0);
        user2.setName("NewName");
        user2.setBirthday(LocalDate.parse("1987-02-05"));
        user2.setLogin("NewLogin");
        user2.setEmail("NewMail@yandex.ru");
        userController.updateUser(user2);
        assertEquals(1, userController.findAll().size(), "Количество пользователей не совпадает");
        assertEquals(user2, userController.findAll().get(0), "Пользователь не обновлён");
    }
}