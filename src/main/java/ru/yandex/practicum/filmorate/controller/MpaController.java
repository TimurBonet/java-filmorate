package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @GetMapping("/id")
    public MPA findById (@PathVariable Long id){
        log.info("GET-запрос на получение рейтинга фильма по id : {}", id);
        return mpaService.findById(id);
    }

    @GetMapping
    public List<MPA> getAllMpa() {
        log.info("GET-запрос на получение списка всех рейтингов");
        List<MPA> mpaList = mpaService.getAllMpa();
        log.info("Получен список всех рейтингов: {}", mpaList.size());
        return mpaList;
    }
}
