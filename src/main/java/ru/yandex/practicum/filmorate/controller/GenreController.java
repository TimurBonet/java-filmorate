package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/id")
    public Genre findGenreById(@PathVariable Long id){
        log.info("GET-запрос на получение жанра фильма по id : {}", id);
        return genreService.findGenreById(id);
    }

    @GetMapping
    public List<Genre> findAllGenres() {
        log.info("GET-запрос на получение списка всех жанров");
        List<Genre> genresList = genreService.findAllGenres();
        log.info("Получен список всех жанров: {}", genresList.size());
        return genresList;
    }
}
