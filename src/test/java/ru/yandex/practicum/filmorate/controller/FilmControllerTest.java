/*
package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.InvalidFilmDataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class FilmControllerTest {

    private final InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    private final InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    private final FilmService filmService = new FilmService(inMemoryFilmStorage, inMemoryUserStorage);
    private FilmController filmController = new FilmController(filmService);
    List<Film> filmsList = new ArrayList<>();
    private static final LocalDate MIN_RELEASEDATE = LocalDate.parse("1895-12-28");
    private final UserService userService = new UserService(inMemoryUserStorage);
    private UserController userController = new UserController(userService);
    List<User> usersList = new ArrayList<>();

    @BeforeEach
    public void beforeEach() {
        User user1 = new User("abc1@mail.ru", "cba", LocalDate.parse("1993-04-15"));
        user1.setName("Kirill");
        usersList.add(user1);

        Film film1 = new Film();
        film1.setName("Jaws");
        film1.setDescription("about sharks");
        film1.setReleaseDate(LocalDate.of(1975, 07, 29));
        film1.setDuration(120);

        Film film2 = new Film();
        film2.setName("Превышен лимит описания");
        film2.setDescription("Действие картины протекает в течение пяти дней — на острове Э́мити," +
                " где расположен небольшой курортный городок. " +
                "В этом тихом и солнечном месте произошли ужасные трагические события, " +
                "начало которым было положено ранним утром, когда шеф местной полиции Мартин Броуди" +
                " и его помощник находят на берегу остатки тела девушки. Она стала первой жертвой " +
                "огромной белой акулы, которая появилась у берегов Эмити… ");
        film2.setReleaseDate(LocalDate.of(1975, 07, 29));
        film2.setDuration(121);

        Film film3 = new Film();
        film3.setName("");
        film3.setDescription("Пустое название");
        film3.setReleaseDate(LocalDate.of(1975, 07, 29));
        film3.setDuration(122);

        Film film4 = new Film();
        film4.setName("Неправильная дата релиза");
        film4.setDescription("about sharks");
        film4.setReleaseDate(LocalDate.of(1875, 07, 29));
        film4.setDuration(122);

        Film film5 = new Film();
        film5.setName("Некорректная продолжительность");
        film5.setDescription("about sharks");
        film5.setReleaseDate(LocalDate.of(1975, 07, 29));
        film5.setDuration(-1);

        Film film6 = new Film();
        film1.setName("Juws");
        film1.setDescription("about juws");
        film1.setReleaseDate(LocalDate.of(1977, 03, 22));
        film1.setDuration(110);

        filmsList.add(film1);
        filmsList.add(film2);
        filmsList.add(film3);
        filmsList.add(film4);
        filmsList.add(film5);
        filmsList.add(film6);
    }

    @Test
    void shouldFindAllFilms() {
        filmController.createFilm(filmsList.get(0));
        List<Film> allFilm = filmController.findAll();
        assertEquals(1, allFilm.size(), "Не соответствует ожидаемому количеству фильмов");
        assertEquals(allFilm.get(0), filmsList.get(0), "Не соответствуют фильмы");
    }

    @Test
    void shouldReturnErrorWithBlankName() {
        Film film = filmsList.get(2);
        assertEquals("", film.getName(), "Поле name должно быть пустым");
        Assertions.assertThrows(InvalidFilmDataException.class, () -> filmController.createFilm(film),
                "Не выбрасывает исключения InvalidFilmDataException при пустом name");

    }

    @Test
    void shouldReturnErrorWithDescriptionOver200() {
        Film film = filmsList.get(1);
        assertEquals(film.getDescription().length() > 200, isMoreThan200(film),
                "Размер описания меньше, либо равен 200 символам");
        Assertions.assertThrows(InvalidFilmDataException.class, () -> filmController.createFilm(film),
                "Не выбрасывает исключения InvalidFilmDataException при description > 200 знаков");
    }

    private boolean isMoreThan200(Film film) {
        if (film.getDescription().length() > 0) {
            return true;
        }
        return false;
    }

    @Test
    void shouldReturnErrorWithUncorrectReleaseDate() {
        Film film = filmsList.get(3);
        assertEquals(film.getReleaseDate().isBefore(MIN_RELEASEDATE), isBeforeReleaseDate(film),
                "Дата не ранее минимальной даты релиза");
        Assertions.assertThrows(InvalidFilmDataException.class, () -> filmController.createFilm(film),
                "Не выбрасывает исключения InvalidFilmDataException при дате релиза ранее минимальной");
    }

    private boolean isBeforeReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASEDATE)) {
            return true;
        }
        return false;
    }

    @Test
    void shouldReturnErrorWithUncorrectDuration() {
        Film film = filmsList.get(4);
        assertEquals(true, isBelowOrEqualZero(film),
                "Продолжительность больше нуля ");
        Assertions.assertThrows(InvalidFilmDataException.class, () -> filmController.createFilm(film),
                "Не выбрасывает исключения InvalidFilmDataException при продолжительности меньше либо равной нуля");
    }

    private boolean isBelowOrEqualZero(Film film) {
        if (film.getDuration() <= 0) {
            return true;
        }
        return false;
    }

    @Test
    void createFilm() {
        Film film = filmsList.get(0);
        filmController.createFilm(film);
        assertEquals(film, filmController.findAll().get(0), "Не совпадают фильмы");
        assertEquals(1, filmController.findAll().size(), "Не совпадает количество");
    }

    @Test
    void updateFilm() {
        Film film1 = filmsList.get(0);
        filmController.createFilm(film1);
        Film film2 = filmController.findAll().get(0);
        film2.setName("DirectorsCut");
        film2.setDuration(140);
        film2.setDescription("New Description");
        film2.setReleaseDate(LocalDate.parse("1988-09-12"));
        filmController.updateFilm(film2);
        assertEquals(1, filmController.findAll().size(), "не совпадает количество фильмов");
        assertEquals(film2, filmController.findAll().get(0), "Не получилось обновить");
    }

    @Test
    void shouldShowTopFilms() {
        userController.createUser(usersList.get(0));
        Film film = filmsList.get(0);
        filmController.createFilm(film);
        Film curFilm = filmController.findAll().get(0);
        Integer idFilm = curFilm.getId();
        filmController.addLike(idFilm, 1);
        assertEquals(1, filmController.findFilmById(idFilm).getLikes().size(), "Некорректное количество лайков");
        List<Film> filmsList = filmController.getTopFilms(1);
        assertEquals(curFilm, filmsList.get(0), "Не совпадают фильмы");
    }


}*/
