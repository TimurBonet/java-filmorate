package ru.yandex.practicum.filmorate.storage;


import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.InvalidFilmDataException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    @lombok.Generated
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FilmController.class);

    private Map<Integer, Film> films = new HashMap<>();
    private Integer userId = 0;

    private static final int DESCRIPTION_MAX_LENGTH = 200;
    private static final String EARLIEST_RELEASE_DATE = "1895-12-28";

    private Integer addId() {
        return ++userId;
    }

    @Override
    public List<Film> findAll() {
        log.info("Количество фильмов в фильмотеке {}", films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findFilmById(Integer id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("Отсутствует фильм с таким id - " + id);
        }
        log.info("Найден фильм с id - {}", id);
        return films.get(id);
    }

    @Override
    public Film createFilm(Film film) throws InvalidFilmDataException, FilmAlreadyExistException {
        log.info("Попытка добавить объект {}", film);

        if (isCorrectForCreate(film)) {
            film.setId(addId());
            films.put(film.getId(), film);
        }

        return film;
    }

    private boolean isCorrectForCreate(Film film) {
        if (film.getName().isBlank()) {
            throw new InvalidFilmDataException("Name is blank");
        }
        if (film.getDescription().length() > DESCRIPTION_MAX_LENGTH) {
            throw new InvalidFilmDataException("Description length = " + film.getDescription().length());
        }

        if (film.getReleaseDate().isBefore(LocalDate.parse(EARLIEST_RELEASE_DATE))) {
            throw new InvalidFilmDataException("Release is " + film.getReleaseDate());
        }

        if (film.getDuration() <= 0) {
            throw new InvalidFilmDataException("Film duration = " + film.getDuration());
        }

        if (films.containsValue(film)) {
            throw new FilmAlreadyExistException(film.getName());
        }

        if (film == null
                || film.getReleaseDate().isBefore(LocalDate.parse(EARLIEST_RELEASE_DATE))
                || film.getDuration() <= 0) {
            throw new InvalidFilmDataException("No film");
        }

        return true;
    }

    @Override
    public Film updateFilm(Film film) throws FilmNotFoundException {
        log.info("Попытка обновить объект {}", film);

        if (isCorrectForUpdate(film)) {
            films.put(film.getId(), film);
        }

        return film;
    }

    private boolean isCorrectForUpdate(Film film) {
        if (films.containsKey(film.getId())) {
            if (film != null || !film.getReleaseDate().isBefore(LocalDate.parse(EARLIEST_RELEASE_DATE))) {
                return true;
            }
        } else /*throw new InvalidFilmDataException("Incorrect data for update");*/
            throw new FilmNotFoundException("Нет фильма для обновления");

        return false;
    }

}
