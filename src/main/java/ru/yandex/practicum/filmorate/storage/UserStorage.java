package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    public List<User> findAll();

    public User findUserById(Integer id);

    public User createUser(User user);

    public User updateUser(User user);

}
