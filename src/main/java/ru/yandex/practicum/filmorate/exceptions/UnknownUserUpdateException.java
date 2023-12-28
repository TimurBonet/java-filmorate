package ru.yandex.practicum.filmorate.exceptions;

public class UnknownUserUpdateException extends Exception {

    public UnknownUserUpdateException() {
        System.out.println("Нет такого пользователя в списке, проверьте правильность запроса.");
    }
}
