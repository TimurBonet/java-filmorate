package ru.yandex.practicum.filmorate.service;


import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotLikedException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    public Film addLike(Integer filmId, Integer id) {
        log.info("Попытка добавить лайк.");
        if (inMemoryUserStorage.findUserById(id) == null) {
            throw new UserNotFoundException("Отсутствует пользователь с таким Id");
        }
        inMemoryFilmStorage.findFilmById(filmId).addLikeFromUser(id);
        Film film = inMemoryFilmStorage.findFilmById(filmId);
        log.info("Лайк от пользователя id - {} добавлен", id);
        return film;
    }

    public Film removeLike(Integer filmId, Integer id) {
        log.info("Попытка удалить лайк.");
        if (!inMemoryFilmStorage.findFilmById(filmId).getLikes().contains(id)) {
            throw new NotLikedException("Пользователь не лайкал фильм");
        }
        inMemoryFilmStorage.findFilmById(filmId).removeLikeFromUser(id);
        Film film = inMemoryFilmStorage.findFilmById(filmId);
        log.info("Лайк от пользователя id - {} удалён", id);
        return film;
    }

    public List<Film> showTopTenFilms(Integer count) {
        log.info("Попытка вывести ТОП-10 фильмов.");

        return inMemoryFilmStorage.findAll().stream()
                .sorted((f1, f2) -> compare(f1, f2))
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film f1, Film f2) {
        int result = f2.getLikes().size() - (f1.getLikes().size());

        return result;
    }


