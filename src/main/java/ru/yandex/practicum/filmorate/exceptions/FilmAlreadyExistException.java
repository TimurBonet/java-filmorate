package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FilmAlreadyExistException extends RuntimeException {

    public FilmAlreadyExistException() {
        System.out.println("Такой фильм уже есть в каталоге.");
    }
}
