package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreService {
    Genre findGenreById (Long genreId);

    List<Genre> findAllGenres();

}
