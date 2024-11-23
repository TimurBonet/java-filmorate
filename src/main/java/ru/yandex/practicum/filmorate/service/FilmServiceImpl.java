package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDAO;
import ru.yandex.practicum.filmorate.storage.UserDAO;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmDAO filmDAO;
    private final UserDAO userDAO;

    @Override
    public void addLike(Integer filmId, Integer userId) {
        isExist(filmId, userId);
        filmDAO.addLike(filmId, userId);
    }

    @Override
    public boolean deleteLike(Integer filmId, Integer userId) {
        isExist(filmId, userId);
        return filmDAO.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        return filmDAO.getTopFilms(count);
    }

    @Override
    public List<Film> findAll() {
        return filmDAO.findAll();
    }

    @Override
    public Film findFilmById(Long id) {
        return filmDAO.findFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм не найден", HttpStatus.NOT_FOUND));
    }

    @Override
    public Film createFilm(Film film) {
        return filmDAO.createFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmDAO.updateFilm(film)
                .orElseThrow(() -> new NotFoundException("Фильм не найден", HttpStatus.NOT_FOUND));
    }

    @Override
    public boolean deleteFilm(Integer filmId) {
        return filmDAO.deleteFilm(filmId);
    }

    private void isExist(Integer filmId, Integer userId) {
        String filmNotFound = "Не найден фильм с ID: ";
        String userNotFound = "Не найден пользователь с ID: ";
        boolean isExistFilm = filmDAO.isExistById(filmId);
        boolean isExistUser = userDAO.isExistById(userId);
        if (!isExistUser && !isExistFilm) {
            throw new NotFoundException(filmNotFound + filmId + " " + userNotFound + userId, HttpStatus.NOT_FOUND);
        } else if (!isExistUser) {
            throw new NotFoundException(userNotFound + userId, HttpStatus.NOT_FOUND);
        } else if (!isExistFilm) {
            throw new NotFoundException(filmNotFound + filmId, HttpStatus.NOT_FOUND);
        }

    }
}
