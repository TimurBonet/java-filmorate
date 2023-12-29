package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnknownUserUpdateException extends RuntimeException {

    public UnknownUserUpdateException() {
        System.out.println("Нет такого пользователя в списке, проверьте правильность запроса.");
    }
}
