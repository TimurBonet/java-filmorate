package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException() {
        System.out.println("Такой пользователь уже существует");
    }
}
