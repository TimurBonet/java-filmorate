package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;


public interface FilmStorage {

    public List<Film> findAll();

    public Film findFilmById(Integer id);

    public Film createFilm(Film film);


    public Film updateFilm(Film film);

}
