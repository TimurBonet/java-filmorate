package ru.yandex.practicum.filmorate.service;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    User getUserById(Integer userId);

    List<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);

    void addFriend(Integer id, Integer friendId);

    void deleteFriend(Integer id, Integer friendId);

    List<User> getFriendList(Integer Id);

    List<User> getCommonFriendList(Integer id, Integer otherId);

}
