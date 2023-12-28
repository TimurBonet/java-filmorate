package ru.yandex.practicum.filmorate.exceptions;

public class InvalidEmailException extends Exception{

    public InvalidEmailException(){
        System.out.println("Отсутствует или неверно задан email! \nПроверьте по критериям: \n" +
                "электронная почта не может быть пустой и должна содержать символ @;\n" +
                "логин не может быть пустым и содержать пробелы;\n" +
                "дата рождения не может быть в будущем.");
    }
}
