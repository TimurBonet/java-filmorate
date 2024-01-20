package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.InvalidFilmDataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.*;

@RestController
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> findAll() {

        return inMemoryFilmStorage.findAll();
    }

    @GetMapping("/films/{id}")
    public Film findFilmById(@PathVariable("id") Integer id) {

        return inMemoryFilmStorage.findFilmById(id);
    }

    @GetMapping("/films/popular")
    public List<Film> showTopTenFilms(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {

        return filmService.showTopTenFilms(count);
    }

    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film) throws InvalidFilmDataException, FilmAlreadyExistException {

        return inMemoryFilmStorage.createFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {

        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film removeLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {

        return filmService.removeLike(id, userId);
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) throws FilmNotFoundException {

        return inMemoryFilmStorage.updateFilm(film);
    }

}
