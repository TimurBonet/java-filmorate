package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmDAO;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Slf4j
@Repository(value = "filmDB")
@RequiredArgsConstructor
public class FilmDAOImpl implements FilmDAO {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> findAll() {
        String sqlQuery = "SELECT f.*, " +
                "m.name as mpa_name, " +
                "GROUP_CONCAT(g.genre_id) as genre_id, " +
                "GROUP_CONCAT(g.name) as genre_name " +
                "FROM films AS f " +
                "LEFT JOIN mpa m ON m.mpa_rating_id = f.mpa_rating_id " +
                "LEFT JOIN film_genres fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre g ON g.genre_id = fg.genre_id " +
                "GROUP BY f.film_id ";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilms);
    }

    @Override
    public Optional<Film> findFilmById(Long id) {
        String sqlQuery = "SELECT f.*, " +
                "m.name as mpa_name, " +
                "GROUP_CONCAT(g.genre_id) as genre_id, " +
                "GROUP_CONCAT(g.name) as genre_name " +
                "FROM films AS f " +
                "LEFT JOIN mpa m ON m.mpa_rating_id = f.mpa_rating_id " +
                "LEFT JOIN film_genres fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre g ON g.genre_id = fg.genre_id " +
                "WHERE f.film_id =  ? " +
                "GROUP by f.film_id";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilms, id);

        return films.stream().findFirst();
    }

    @Override
    public Film createFilm(Film film) {

        if (film.getMpa().getId() > 5) {
            throw new NotFoundException("Некорректный возрастной рейтинг", HttpStatus.BAD_REQUEST);
        }
        log.info("Запрос в DbStorage, создания фильма: {}", film);

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                if (genre.getId() > 6) {
                    throw new NotFoundException("Некорректный жанр", HttpStatus.BAD_REQUEST);
                }
            }
        }
        String sqlQuery = "INSERT INTO films (name, description, releaseDate, duration, mpa_rating_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
                statement.setString(1, film.getName());
                statement.setString(2, film.getDescription());
                statement.setDate(3, Date.valueOf(film.getReleaseDate()));
                statement.setInt(4, film.getDuration().intValue());
                statement.setLong(5, film.getMpa().getId());

                return statement;
            }, keyHolder);

            Long filmId = Objects.requireNonNull(keyHolder.getKey()).longValue();
            log.info("ATTENTION" + filmId);
            film.setId(filmId);
            updateFilmGenresList(film);

            log.info("Добавлен фильм: {}", film);
            return film;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, releaseDate = ?, " +
                "duration = ?, mpa_rating_id = ? " +
                "WHERE film_id = ?";
        int update = jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        if (update == 0) {
            throw new NotFoundException("Фильм не найден", HttpStatus.NOT_FOUND);
        }
        updateFilmGenresList(film);
        Film film1 = findFilmById(film.getId()).orElse(null);
        log.info("Проверка апдейта {}", film1);
        return findFilmById(film.getId());
    }

    @Override
    public boolean addLike(Integer filmId, Integer userId) {
        String sqlQuery = "INSERT INTO FILM_LIKES (user_id,film_id) " +
                "VALUES (?,?) ";
        try {
            jdbcTemplate.update(sqlQuery, userId, filmId);
            return true;
        } catch (DuplicateKeyException e) {
            return false;
        }
    }

    @Override
    public boolean isExistById(Integer id) {
        String sqlQuery = "SELECT EXISTS (SELECT 1 FROM films WHERE film_id = ?) ";
        return jdbcTemplate.queryForObject(sqlQuery, Boolean.class, id);
    }

    @Override
    public boolean deleteFilm(Integer filmId) {
        String sqlQuery = "DELETE FROM films " +
                "WHERE film_id = ?";
        return jdbcTemplate.update(sqlQuery, filmId) > 0;
    }

    @Override
    public boolean deleteLike(Integer filmId, Integer userId) {
        String sqlQuery = "DELETE FROM FILM_LIKES " +
                "WHERE film_id = ? AND user_id = ? ";
        return jdbcTemplate.update(sqlQuery, filmId, userId) > 0;
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        String sqlQuery = "SELECT f.*, " +
                "m.name as mpa_name, " +
                "GROUP_CONCAT(g.genre_id) as genre_id, " +
                "GROUP_CONCAT(g.name) as genre_name, " +
                "COUNT(FL.user_id) " +
                "FROM films AS f " +
                "LEFT JOIN mpa m ON m.mpa_rating_id = f.mpa_rating_id " +
                "LEFT JOIN film_genres fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre g ON g.genre_id = fg.genre_id " +
                "LEFT JOIN FILM_LIKES FL ON f.film_id = FL.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(FL.user_id) DESC " +
                "LIMIT ? ";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilms, count);
    }

    private Film mapRowToFilms(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("releaseDate").toLocalDate())
                .duration(resultSet.getLong("duration"))
                .mpa(mapRowToMpa(resultSet)) // mpa(mapRowToMpa(MpaDAOImpl.findById(resultSet.getLong("mpa_rating_id")))
                .genres(mapRowToGenre(resultSet))
                .build();
    }

    private Set<Genre> mapRowToGenre(ResultSet resultSet) throws SQLException {
        Set<Genre> genreList = new HashSet<>();
        String genreId = resultSet.getString("genre_id");
        String genreName = resultSet.getString("genre_name");
        if (genreId != null) {
            String[] genreIds = genreId.split(",");
            String[] genreNames = genreName.split(",");
            for (int i = 0; i < genreIds.length; i++) {
                if (i < genreNames.length) {
                    genreList.add(Genre.builder()
                            .id(Long.parseLong(genreIds[i]))
                            .name(genreNames[i])
                            .build());
                }
            }
        }
        return genreList;
    }

    private MPA mapRowToMpa(ResultSet resultSet) throws SQLException {
        return MPA.builder()
                .id(resultSet.getLong("mpa_rating_id"))
                .name(resultSet.getString("mpa_name")) //name
                .build();
    }


    private void deleteFilmGenresList(Long filmId) {
        String sqlQuery = "DELETE FROM film_genres " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    private void updateFilmGenresList(Film film) {
        if (film.getGenres() == null) {
            return;
        }
        deleteFilmGenresList(film.getId());
        String sqlQuery = "INSERT INTO film_genres (film_id,genre_id) " +
                "VALUES(?,?)";
        Set<Genre> genres = new TreeSet<>(Comparator.comparingLong(Genre::getId));
        genres.addAll(film.getGenres());
        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Genre genre = new ArrayList<>(genres).get(i);
                ps.setLong(1, film.getId());
                ps.setLong(2, genre.getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
    }


}

