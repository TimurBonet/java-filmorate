package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> findAll();

    User findUserById(Integer id);

    User createUser(User user);

    User updateUser(User user);

    boolean addFriend(Integer id, Integer friendId);

    boolean deleteFriend(Integer id, Integer friendId);

    List<User> getFriendList (Integer id);

    List<User> getCommonFriends (Integer id, Integer otherId);

    boolean isExistById(Integer id);

    boolean isExist(Integer id);

}
