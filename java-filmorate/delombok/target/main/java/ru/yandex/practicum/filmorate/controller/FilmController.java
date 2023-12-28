package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.InvalidFilmDataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.*;

@RestController
public class FilmController {
    @lombok.Generated
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FilmController.class);
    //private static final List<Film> films = new ArrayList<>();
    private Map<Integer, Film> films = new HashMap<>();
    private int userId = 0;

    public int addId() {
        return ++userId;
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        log.info("Количество фильмов в фильмотеке {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping("/films")
    public Film createFilm(@RequestBody Film film) throws InvalidFilmDataException, FilmAlreadyExistException {
        log.info("Попытка добавить объект {}", film);
        if (!films.containsValue(film)) {
            if (
            //|| film.getName() != null
            film != null || film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
                /*|| !film.getDuration().isNegative()*/film.setId(addId());
                films.put(film.getId(), film);
            } else throw new InvalidFilmDataException();
        } else throw new FilmAlreadyExistException();
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) throws InvalidFilmDataException {
        log.info("Попытка обновить объект {}", film);
        if (films.containsKey(film.getId())) {
            if (
            //|| film.getName() != null
            //|| film.getDescription().length() > 200
            film != null || film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
                /*|| !film.getDuration().isNegative()*/films.put(film.getId(), film);
            } else throw new InvalidFilmDataException();
        } else films.put(film.getId(), film);
        return film;
    }
}
