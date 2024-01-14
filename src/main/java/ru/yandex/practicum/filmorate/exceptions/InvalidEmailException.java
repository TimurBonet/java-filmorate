package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidEmailException extends RuntimeException {

    public InvalidEmailException() {
        System.out.println("Отсутствует или неверно задан email! \nПроверьте по критериям: \n" +
                "электронная почта не может быть пустой и должна содержать символ @;\n" +
                "логин не может быть пустым и содержать пробелы;\n" +
                "дата рождения не может быть в будущем.");
    }
}
