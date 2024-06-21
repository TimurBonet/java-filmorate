package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.dao.FilmDAOImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {FilmDAOImpl.class})
@ComponentScan(basePackages = {"ru.yandex.practicum.filmorate.dao"})
public class FilmDAOImplTest {
    private final FilmDAOImpl filmDAOstorage;

    @Test
    void createFilmTestOk() {
        final Film film = generateFilm("film1", "description1",
                LocalDate.of(1967, 12, 11), 123L, generateMPA(1L, "G"));

        filmDAOstorage.createFilm(film);

        final List<Film> films = filmDAOstorage.findAll();

        MPA currentMPA = generateMPA(1L, "G");

        assertEquals(1, films.size());
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("description", "description1");
        assertThat(films.get(0)).hasFieldOrProperty("releaseDate");
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("duration", 123L);
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("mpa", currentMPA);
    }

    @Test
    void updateFilmTestOk() {
        final Film oldFilm = generateFilm("film1", "description1",
                LocalDate.of(1967, 12, 11), 123L, generateMPA(1L, "G"));
        filmDAOstorage.createFilm(oldFilm);
        final Film film = generateFilm("film2", "description2",
                LocalDate.of(1968, 9, 12), 145L, generateMPA(3L, "PG-13"));
        film.setId(4L);

        MPA currentMPA = generateMPA(3L, "PG-13");

        filmDAOstorage.updateFilm(film);

        final Optional<Film> updatedFilm = filmDAOstorage.findFilmById(4L);

        assertThat(updatedFilm.get()).hasFieldOrPropertyWithValue("id", 4L);
        assertThat(updatedFilm.get()).hasFieldOrPropertyWithValue("name", "film2");
        assertThat(updatedFilm.get()).hasFieldOrPropertyWithValue("description", "description2");
        assertThat(updatedFilm.get()).hasFieldOrProperty("releaseDate");
        assertThat(updatedFilm.get()).hasFieldOrPropertyWithValue("duration", 145L);
        assertThat(updatedFilm.get()).hasFieldOrPropertyWithValue("mpa", currentMPA);
    }

    @Test
    void getFilmByIdTest() {
        final Film testFilm = generateFilm("film1", "description1",
                LocalDate.of(1967, 12, 11), 123L, generateMPA(1L, "G"));

        filmDAOstorage.createFilm(testFilm);

        Optional<Film> film = filmDAOstorage.findFilmById(5L);

        MPA currentMPA = generateMPA(1L, "G");

        assertThat(film.get()).hasFieldOrPropertyWithValue("id", 5L);
        assertThat(film.get()).hasFieldOrPropertyWithValue("name", "film1");
        assertThat(film.get()).hasFieldOrPropertyWithValue("description", "description1");
        assertThat(film.get()).hasFieldOrProperty("releaseDate");
        assertThat(film.get()).hasFieldOrPropertyWithValue("duration", 123L);
        assertThat(film.get()).hasFieldOrPropertyWithValue("mpa", currentMPA);
    }

    @Test
    void getAllUsersTest() {
        final Film film1 = generateFilm("film1", "description1",
                LocalDate.of(1967, 12, 11), 123L, generateMPA(1L, "G"));
        filmDAOstorage.createFilm(film1);
        final Film film2 = generateFilm("film2", "description2",
                LocalDate.of(1968, 9, 12), 145L, generateMPA(3L, "PG-13"));
        filmDAOstorage.createFilm(film2);
        List<Film> films = filmDAOstorage.findAll();

        MPA mpa1 = generateMPA(1L, "G");
        MPA mpa2 = generateMPA(3L, "PG-13");

        assertEquals(2, films.size());
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("id", 2L);
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("name", "film1");
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("description", "description1");
        assertThat(films.get(0)).hasFieldOrProperty("releaseDate");
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("duration", 123L);
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("mpa", mpa1);

        assertThat(films.get(1)).hasFieldOrPropertyWithValue("id", 3L);
        assertThat(films.get(1)).hasFieldOrPropertyWithValue("name", "film2");
        assertThat(films.get(1)).hasFieldOrPropertyWithValue("description", "description2");
        assertThat(films.get(1)).hasFieldOrProperty("releaseDate");
        assertThat(films.get(1)).hasFieldOrPropertyWithValue("duration", 145L);
        assertThat(films.get(1)).hasFieldOrPropertyWithValue("mpa", mpa2);
    }


    @Test
    void deleteUsersTest() {
        final Film film1 = generateFilm("film1", "description1",
                LocalDate.of(1967, 12, 11), 123L, generateMPA(1L, "G"));
        filmDAOstorage.createFilm(film1);
        final Film film2 = generateFilm("film2", "description2",
                LocalDate.of(1968, 9, 12), 145L, generateMPA(3L, "PG-13"));
        filmDAOstorage.createFilm(film2);

        filmDAOstorage.deleteFilm(7);
        List<Film> films = filmDAOstorage.findAll();

        MPA mpa1 = generateMPA(1L, "G");

        assertEquals(1, films.size());
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("id", 6L);
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("name", "film1");
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("description", "description1");
        assertThat(films.get(0)).hasFieldOrProperty("releaseDate");
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("duration", 123L);
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("mpa", mpa1);

    }

    private Film generateFilm(
            String name,
            String description,
            LocalDate releaseDate,
            Long duration,
            MPA mpa) {
        return Film.builder()
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .mpa(mpa)
                .build();
    }

    private MPA generateMPA(
            Long mpaId,
            String mpaName
    ) {
        return MPA.builder()
                .id(mpaId)
                .name(mpaName)
                .build();
    }
}
