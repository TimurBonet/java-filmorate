package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDAO;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;

    @Override
    @ExceptionHandler
    public User getUserById(Integer userId) {
        User user = userDAO.findUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользоватль id : " + userId + " отсутствует в списке", HttpStatus.NOT_FOUND);
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    @Override
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userDAO.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userDAO.updateUser(user);
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        log.info("(UserServiceImpl->addFriend)Добавляем в друзья пользователю {}, друга {}", userId, friendId);
        checkId(userId, friendId);
        boolean isAdded = userDAO.addFriend(userId, friendId);
        boolean isAddedFriend = userDAO.addFriend(friendId, userId);
        if (!isAdded) {
            throw new BadRequestException("(UserServiceImpl->addFriend)Пользователь не добавлен в друзья", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        checkId(userId, friendId);
        userDAO.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> getFriendList(Integer id) {
        if (!userDAO.isExistById(id)) {
            throw new NotFoundException("Не найден пользователь id: " + id, HttpStatus.NOT_FOUND);
        }
        return userDAO.getFriendList(id);

    }

    @Override
    public List<User> getCommonFriendList(Integer userId, Integer otherId) {
        checkId(userId, otherId);
        return userDAO.getCommonFriends(userId, otherId);
    }

    private void checkId(Integer id1, Integer id2) {
        User user1 = userDAO.findUserById(id1);

        if (user1 == null) {
            throw new NotFoundException("Пользователь id: " + id1 + " не найден.", HttpStatus.NOT_FOUND);
        }

        log.info("(UserServiceImpl->checkId)Юзер 1 найден : {}", user1);


        User user2 = userDAO.findUserById(id2);

        if (user2 == null) {
            throw new NotFoundException("Пользователь id: " + id2 + " не найден.", HttpStatus.NOT_FOUND);
        }

        log.info("(UserServiceImpl->checkId)Юзер 2 найден : {}", user2);

    }
}
