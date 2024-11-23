package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        log.info("GET-запрос на получения списка всех пользователей.");
        List<User> allUsersList = userService.getAllUsers();
        log.info("Получен список из {} пользователей", allUsersList.size());
        return allUsersList;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") int id) {
        User user = userService.getUserById(id);
        log.info("GET-запрос на получения пользователя по id : {}. " +
                "В результате получен пользователь id = {}, name = {}",id, user.getName());
        return user;
    }

    @GetMapping("/{id}/friends/common/{friend_id}")
    public List<User> getCommonFriendList(@PathVariable("id") int id, @PathVariable("friend_id") int otherId) {
        return userService.getCommonFriendList(id, otherId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendList(@PathVariable("id") Integer id) {
        return userService.getFriendList(id);
    }

    @PutMapping("/{id}/friends/{friend_id}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable("id") int id, @PathVariable("friend_id") int friendId) {
        userService.addFriend(id, friendId);
        log.info("Пользователю id: {}, добавлен друг id: {}", id, friendId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        log.info("POST-запрос на создание пользователя {}", user.getName());
        User createdUser = userService.createUser(user);
        log.info("Пользователь {} создан.", createdUser.getName());
        return createdUser;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@Valid @RequestBody User user) {
        log.info("PUT-запрос на обновление пользователя id = {}, name {}", user.getId(), user.getName());
        User updatedUser = userService.updateUser(user);
        log.info("Пользователь {} обновлён.", updatedUser.getName());
        return updatedUser;
    }

    @DeleteMapping("/{id}/friends/{friend_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFriend(@PathVariable("id") int id, @PathVariable("friend_id") int friendId) {
        userService.deleteFriend(id, friendId);
        log.info("Пользователю {} удалён друг id: {}", id, friendId);
    }

}
