package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
public class FilmServiceImpl  implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmServiceImpl (FilmStorage filmDAO, UserStorage userDAO) {
        this.filmStorage = filmDAO;
        this.userStorage = userDAO;
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        isExist(filmId,userId);
        filmStorage.addLike(filmId,userId);
    }

    @Override
    public boolean deleteLike(Integer filmId, Integer userId) {
        isExist(filmId, userId);
        return filmStorage.deleteLike(filmId,userId);
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        return filmStorage.getTopFilms(count);
    }

    @Override
    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    @Override
    public Film findFilmById(Long id) {
        return filmStorage.findFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм не найден",HttpStatus.NOT_FOUND));
    }

    @Override
    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film).
                orElseThrow(() -> new NotFoundException("Фильм не найден", HttpStatus.NOT_FOUND));
    }

    @Override
    public boolean deleteFilm(Integer filmId) {
        return filmStorage.deleteFilm(filmId);
    }

    private void isExist (Integer filmId, Integer userId)  {
        String filmNotFound = "Не найден фильм с ID: ";
        String userNotFound = "Не найден пользователь с ID: ";
        boolean isExistFilm = filmStorage.isExistById(filmId);
        boolean isExistUser = userStorage.isExistById(userId);
        if (!isExistUser && ! isExistFilm) {
            throw new NotFoundException(filmNotFound + filmId + " " + userNotFound + userId, HttpStatus.NOT_FOUND);
        } else if (!isExistUser) {
            throw new NotFoundException(userNotFound + userId,HttpStatus.NOT_FOUND);
        } else if(!isExistFilm) {
            throw new NotFoundException(filmNotFound + filmId,HttpStatus.NOT_FOUND );
        }

    }
}
