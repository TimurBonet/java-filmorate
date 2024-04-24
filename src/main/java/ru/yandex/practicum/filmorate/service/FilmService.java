package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;


public interface FilmService {
    List<Film> findAll();

    Film findFilmById(Long id);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void addLike (Integer filmId, Integer userId);

    boolean deleteFilm(Integer filmId);

    boolean deleteLike(Integer filmId, Integer userId);

    List<Film> getTopFilms(Integer count);

}
