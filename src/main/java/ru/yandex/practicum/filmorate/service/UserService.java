package ru.yandex.practicum.filmorate.service;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    User getUserById(Integer userId);

    List<User> getAllUsers();


    User createUser(User user);
=======
    public User deleteFriend(Integer id, Integer friendId) {
        User currentUser = inMemoryUserStorage.findUserById(id);
        User friend = inMemoryUserStorage.findUserById(friendId);
        if (currentUser.getFriends().contains(friendId)) {
            currentUser.removeIdUserFriend(friendId);
            friend.removeIdUserFriend(id);
        }
        log.info("Попытка удалить пользователя id - {} из друзей. Пользователь id - {} друзья: {}",
                friendId, id, inMemoryUserStorage.findUserById(id).getFriends());
        log.info("Пользователь id - {} друзья: {}",
                friendId, inMemoryUserStorage.findUserById(id).getFriends());
        return friend;
    }



