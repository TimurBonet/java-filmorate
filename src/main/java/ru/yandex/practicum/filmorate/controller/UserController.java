package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.InvalidEmailException;
import ru.yandex.practicum.filmorate.exceptions.UnknownUserUpdateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;


import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserService userService;

    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> findAll() {

        return inMemoryUserStorage.findAll();
    }

    @GetMapping("/users/{id}")
    public User findById(@PathVariable("id") Integer id) {

        return inMemoryUserStorage.findUserById(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> showCommonFriendList(@PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId) {

        return userService.showCommonFriendList(id, otherId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> showFriendList(@PathVariable("id") Integer id) {

        return userService.showFriendList(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {

        return userService.addFriend(id, friendId);
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) throws InvalidEmailException {

        return inMemoryUserStorage.createUser(user);
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) throws InvalidEmailException, UnknownUserUpdateException {

        return inMemoryUserStorage.updateUser(user);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {

        return userService.deleteFriend(id, friendId);
    }

}
