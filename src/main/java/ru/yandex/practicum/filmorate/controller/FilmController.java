package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping("/films")
    public List<Film> findAll() {

        return filmService.findAll();
    }

    @GetMapping("/films/{id}")
    public Film findFilmById(@PathVariable("id") Integer id) {

        return filmService.findFilmById(id);
    }

    @GetMapping("/films/popular")
    public List<Film> showTopTenFilms(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {

        return filmService.showTopTenFilms(count);
    }

    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film) {

        return filmService.createFilm(film);
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
    public Film updateFilm(@Valid @RequestBody Film film) {

        return filmService.updateFilm(film);
    }

}
