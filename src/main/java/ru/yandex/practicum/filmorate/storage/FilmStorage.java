package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.InvalidFilmDataException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface FilmStorage {

    public List<Film> findAll();

    public Film findFilmById(Integer id);

    public Film createFilm(Film film);


    public Film updateFilm(Film film);

}
