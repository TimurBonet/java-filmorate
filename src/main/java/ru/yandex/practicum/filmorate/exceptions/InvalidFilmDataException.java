package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidFilmDataException extends RuntimeException {

    public InvalidFilmDataException() {
        System.out.println("Некорректные данные фильма! \nПроверьте соответствие критериям: " +
                "\n название не может быть пустым;\n" +
                "максимальная длина описания — 200 символов;\n" +
                "дата релиза — не раньше 28 декабря 1895 года;\n" +
                "продолжительность фильма должна быть положительной.");
    }
}
