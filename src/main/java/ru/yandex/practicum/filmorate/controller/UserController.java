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
        log.info("Получен список из {} пользователей",allUsersList.size());
        return allUsersList;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Integer id) {
        log.info("GET-запрос на получения пользователя по id : {}", id);
        User user1 = userService.getUserById(id);
        log.info("Получен пользователь : {}", user1);
        return user1;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriendList(@PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId) {
        return userService.getCommonFriendList(id, otherId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendList(@PathVariable("id") Integer id) {
        return userService.getFriendList(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        userService.addFriend(id, friendId);
        log.info("Пользователю id: {}, добавлен друг id: {}",id,friendId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        log.info("POST-запрос на создание пользователя {}", user.getName());
        User user1 = userService.createUser(user);
        log.info("Пользователь {} создан.", user1.getName());
        return user1;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("PUT-запрос на обновление пользователя {}", user.getName());
        User user1 = userService.updateUser(user);
        log.info("Пользователь {} обновлён.", user1.getName());
        return user1;
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        userService.deleteFriend(id, friendId);
        log.info("Пользователю удалён друг id: {}", id);
    }

}
