package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;


@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<Film> findAll() {
        log.info("GET - запрос на список всех фильмов");
        List<Film> filmsList = filmService.findAll();
        log.info("Получен список из {} фильмов.", filmsList.size());
        return filmsList;
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable long id) {
        log.info("Запрос фильма по id : {}", id);
        return filmService.findFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@Positive @RequestParam(defaultValue = "10") String count) {
        log.info("GET - запрос на получение топ {} фильмов.", Integer.valueOf(count));
        List<Film> topFilmsList = filmService.getTopFilms(Integer.valueOf(count));
        log.info("Получен топ {} список из {} фильмов.", count, topFilmsList.size());
        return topFilmsList;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("POST- запрос, создания фильма: {}", film);
        return filmService.createFilm(film);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("PUT-запрос на обновление данных фильма: {}", film);
        Film film1 = filmService.updateFilm(film);
        log.info("Добавлен либо обновлён фильм: {}", film1);
        return film1;
    }

    @DeleteMapping("/id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFilm(@PathVariable int id) {
        log.info("DELETE-запрос на удаление фильма по id: {}", id);
        filmService.deleteFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        log.info("PUT-запрос на добавление лайка, по id : {}", userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteLike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        log.info("DELETE-запрос на удаление лайков по id: {}", id);
        return filmService.deleteLike(id, userId);
    }

}
