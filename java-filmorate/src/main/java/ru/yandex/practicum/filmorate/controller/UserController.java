package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.InvalidEmailException;
import ru.yandex.practicum.filmorate.exceptions.UnknownUserUpdateException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
public class UserController {
    @lombok.Generated
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);
    private Map<Integer, User> users = new HashMap<>();
    private int userId = 0;

    public int addId() {
        return ++userId;
    }

    @GetMapping("/users")
    public List<User> findAll() {
        log.info("Попытка найти список пользователей, пользователей в списке: {}", users.size());
        List<User> userList = new ArrayList<>();
        for (User u : users.values()) {
            userList.add(u);
        }
        return userList;
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) throws InvalidEmailException {
        log.info("Попытка добавить объект {}", user);
        if (user.getEmail().contains("@") && !user.getEmail().contains(" ") && !user.getBirthday().isAfter(LocalDate.now())) {
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            user.setId(addId());
            users.put(user.getId(), user);
        } else {
            throw new InvalidEmailException();
        }
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) throws InvalidEmailException, UnknownUserUpdateException {
        log.info("Попытка обновить объект {}", user);
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getEmail().contains("@") && !user.getEmail().contains(" ") && !user.getBirthday().isAfter(LocalDate.now())) {
            if (!users.containsKey(user.getId())) {
                throw new UnknownUserUpdateException();
            } else {
                users.put(user.getId(), user);
            }
        } else {
            throw new InvalidEmailException();
        }
        return user;
    }
}
