package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.InvalidEmailException;
import ru.yandex.practicum.filmorate.exceptions.UnknownUserUpdateException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    @lombok.Generated
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);
    private Map<Integer, User> users = new HashMap<>();
    private int userId = 0;

    public int addId() {
        return ++userId;
    }

    @Override
    public List<User> findAll() {
        log.info("Попытка вывести список пользователей");
        List<User> userList = new ArrayList<>();
        for (User u : users.values()) {
            userList.add(u);
        }
        log.info("Список найден, пользователей в списке: {}", users.size());
        return userList;
    }

    @Override
    public User findUserById(Integer id) {
        log.info("Попытка найти пользователя по id");
        if(!users.containsKey(id)){
            throw new UserNotFoundException("нет пользователя с id - " + id);
        }
        log.info("Найден пользователь User : {}", users.get(id));
        return users.get(id);
    }

    @Override
    public User createUser(User user) throws InvalidEmailException {
        log.info("Попытка добавить пользователя");
        User currentUser = user;
        if (isCorrectForCreate(user)) {
            currentUser.setId(addId());
            if (currentUser.getName() == null || currentUser.getName().isBlank()) {
                currentUser.setName(user.getLogin());
            }
            users.put(currentUser.getId(), currentUser);
        } else {
            throw new InvalidEmailException(currentUser.getEmail());
        }

        log.info("Пользователь {} добавлен.", currentUser);
        return currentUser;
    }

    private boolean isCorrectForCreate(User user) {
        if (user.getEmail().contains("@")
                && !user.getEmail().contains(" ")
                && !user.getBirthday().isAfter(LocalDate.now())) {
            return true;
        }
        return false;
    }

    @Override
    public User updateUser(User user) throws InvalidEmailException, UnknownUserUpdateException {
        log.info("Попытка обновить пользователя");
        if (isCorrectForUpdate(user)) {
            users.put(user.getId(), user);
        }
        log.info("Пользователь {} обновлён", user);
        return user;
    }

    private boolean isCorrectForUpdate(User user) {

        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if(!users.containsKey(user.getId())) {
            throw new UserNotFoundException("Отсутствует пользователь с id - " + user.getId());
        }

        if (user.getEmail().contains("@") && !user.getEmail().contains(" ") && !user.getBirthday().isAfter(LocalDate.now())) {
            if (!users.containsKey(user.getId())) {
                throw new UnknownUserUpdateException("Некорректные данные пользователя");
            } else {
                return true;
            }
        } else {
            throw new InvalidEmailException(user.getEmail());
        }
    }
}

