package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidFilmDataException extends RuntimeException {

    public InvalidFilmDataException(String message) {
        super(message);
    }
}
