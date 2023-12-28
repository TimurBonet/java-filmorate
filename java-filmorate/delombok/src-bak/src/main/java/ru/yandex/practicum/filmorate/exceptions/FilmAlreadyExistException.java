package ru.yandex.practicum.filmorate.exceptions;

public class FilmAlreadyExistException extends Exception{

    public FilmAlreadyExistException(){
        System.out.println("Такой фильм уже есть в каталоге.");
    }
}
