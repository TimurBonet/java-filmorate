package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    @ExceptionHandler
    public User getUserById(Integer userId) {
        User user = userStorage.findUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользоватль id : " + userId + " отсутствует в списке", HttpStatus.NOT_FOUND);
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.findAll();
    }

    @Override
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userStorage.updateUser(user);
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        checkId(userId, friendId);
        boolean isAdded = userStorage.addFriend(userId, friendId);
        if (!isAdded) {
            throw new BadRequestException("Пользователь уже добавлен в друзья", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        checkId(userId, friendId);
        userStorage.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> getFriendList(Integer id) {
        if (!userStorage.isExistById(id)) {
            throw new NotFoundException("Не найден пользователь id: " + id, HttpStatus.NOT_FOUND);
        }
        return userStorage.getFriendList(id);
    }

    @Override
    public List<User> getCommonFriendList(Integer userId, Integer otherId) {
        checkId(userId, otherId);
        return userStorage.getCommonFriends(userId, otherId);
    }

    private void checkId(Integer id1, Integer id2) {
        User user1 = userStorage.findUserById(id1);
        User user2 = userStorage.findUserById(id2);

        if (user1 == null && user2 == null) {
            throw new NotFoundException("Пользователь id: " + id1 + ", пользователь id: " + id2 + " не найдены", HttpStatus.NOT_FOUND);
        }

        if (user1 == null) {
            throw new NotFoundException("Пользователь id: " + id1 + " не найден.", HttpStatus.NOT_FOUND);
        }

        if (user2 == null) {
            throw new NotFoundException("Пользователь id: " + id2 + " не найден.", HttpStatus.NOT_FOUND);
        }
    }
}
