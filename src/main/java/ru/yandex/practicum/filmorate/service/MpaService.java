package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface MpaService {
    MPA findById(Long mpaId);

    List<MPA> getAllMpa();
}
