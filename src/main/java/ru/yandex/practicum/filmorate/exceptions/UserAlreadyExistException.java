package ru.yandex.practicum.filmorate.exceptions;

public class UserAlreadyExistException extends Exception {

    public UserAlreadyExistException() {
        System.out.println("Такой пользователь уже существует");
    }
}
