package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;


public interface FilmDAO {

    List<Film> findAll();

    Optional<Film> findFilmById(Long id);

    Film createFilm(Film film);

    Optional<Film> updateFilm(Film film);

    boolean addLike(Integer filmId, Integer userId);

    boolean isExistById(Integer id);

    boolean deleteFilm(Integer filmId);

    boolean deleteLike(Integer filmId, Integer userId);

    List<Film> getTopFilms(Integer count);

}
