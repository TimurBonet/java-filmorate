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
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Slf4j
@Repository//(value = "filmData")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> findAll() {
        String sqlQuery = "SELECT F.*, " +
                "M.name as mpa_name, " +
                "GROUP_CONCAT(G.GENRE_ID) as genre_id, " +
                "GROUP_CONCAT(G.NAME) as genre_name " +
                "FROM FILMS AS F " +
                "LEFT JOIN MPA M ON M.MPA_RATING_ID = F.MPA_RATING_ID " +
                "LEFT JOIN FILM_GENRES FG ON F.FILM_ID = FG.FILM_ID " +
                "LEFT JOIN GENRE G ON G.GENRE_ID = FG.GENRE_ID " +
                "GROUP BY F.FILM_ID ";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilms);
    }

    @Override
    public Optional<Film> findFilmById(Long id) {
        String sqlQuery = "SELECT F.*, " +
                "M.name as mpa_name, " +
                "GROUP_CONCAT(G.GENRE_ID) as genre_id, " +
                "GROUP_CONCAT(G.NAME) as genre_name " +
                "FROM FILMS AS F " +
                "LEFT JOIN MPA M ON M.MPA_RATING_ID = F.MPA_RATING_ID " +
                "LEFT JOIN FILM_GENRES FG ON F.FILM_ID = FG.FILM_ID " +
                "LEFT JOIN GENRE G ON G.GENRE_ID = FG.GENRE_ID " +
                "WHERE F.FILM_ID =  ? " +
                "GROUP by F.FILM_ID";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilms, id);

        return films.stream().findFirst();
    }

    @Override
    public Film createFilm(Film film) {

        if (film.getMpaRating().getMpaRatingId() > 5) {
            throw new NotFoundException("Некорректный возрастной рейтинг", HttpStatus.BAD_REQUEST);
        }
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                if (genre.getGenreId() > 6) {
                    throw new NotFoundException("Некорректный жанр", HttpStatus.BAD_REQUEST);
                }
            }
        }
        String sqlQuery = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASEDATE, DURATION, MPA_RATING_ID) " +
                "VALUES (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
                statement.setString(1, film.getName());
                statement.setString(2, film.getDescription());
                statement.setDate(3, Date.valueOf(film.getReleaseDate()));
                statement.setInt(4, film.getDuration().intValue());
                statement.setInt(5, film.getMpaRating().getMpaRatingId().intValue());
                return statement;
            }, keyHolder);
            Long filmId = Objects.requireNonNull(keyHolder.getKey().longValue());
            film.setId(filmId);
            updateFilmGenresList(film);
            return film;
        } catch (NotFoundException e) {
            return null;
        }
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        String sqlQuery = "UPDATE FILMS SET " +
                "NAME = ?, DESCRIPTION = ?, RELEASEDATE = ?, " +
                "DURATION = ?, MPA_RATING_ID = ? " +
                "WHERE FILM_ID = ?";
        int update = jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpaRating().getMpaRatingId(), film.getId());
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
        String sqlQuery = "INSERT INTO FILM_LIKES (USER_ID,FILM_ID) " +
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
        String sqlQuery = "SELECT EXISTS (SELECT 1 FROM FILMS WHERE FILM_ID = ?) ";
        return jdbcTemplate.queryForObject(sqlQuery, Boolean.class, id);
    }

    @Override
    public boolean deleteFilm(Integer filmId) {
        String sqlQuery = "DELETE FROM FILMS " +
                "WHERE FILM_ID = ?";
        return jdbcTemplate.update(sqlQuery, filmId) > 0;
    }

    @Override
    public boolean deleteLike(Integer filmId, Integer userId) {
        String sqlQuery = "DELETE FROM FILM_LIKES " +
                "WHERE FILM_ID = ? AND USER_ID = ? ";
        return jdbcTemplate.update(sqlQuery, filmId, userId) > 0;
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        String sqlQuery = "SELECT F.*, " +
                "M.NAME as mpa_name, " +
                "GROUP_CONCAT(G.GENRE_ID) as genre_id, " +
                "GROUP_CONCAT(G.NAME) as genre_name, " +
                "COUNT(FL.USER_ID) " +
                "FROM FILMS AS F " +
                "LEFT JOIN MPA M ON M.MPA_RATING_ID = F.MPA_RATING_ID " +
                "LEFT JOIN FILM_GENRES FG ON F.FILM_ID = FG.FILM_ID " +
                "LEFT JOIN GENRE G ON G.GENRE_ID = FG.GENRE_ID " +
                "LEFT JOIN FILM_LIKES FL ON F.FILM_ID = FL.FILM_ID " +
                "GROUP BY F.FILM_ID " +
                "ORDER BY COUNT(FL.USER_ID) DESC " +
                "LIMIT ? ";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilms, count);
    }

    private Film mapRowToFilms(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("FILM_ID"))
                .name(resultSet.getString("NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .releaseDate(resultSet.getDate("RELEASEATE").toLocalDate())
                .duration(resultSet.getLong("DURATION"))
                .mpaRating(mapRowToMpa(resultSet))
                .genres(mapRowToGenre(resultSet))
                .build();
    }

    private Set<Genre> mapRowToGenre(ResultSet resultSet) throws SQLException {
        Set<Genre> genreList = new HashSet<>();
        String genreId = resultSet.getString("GENRE_ID");
        String genreName = resultSet.getString("NAME");
        if (genreId != null) {
            String[] genreIds = genreId.split(",");
            String[] genreNames = genreName.split(",");
            for (int i = 0; i < genreIds.length; i++) {
                if (i < genreNames.length) {
                    genreList.add(Genre.builder()
                            .genreId(Long.parseLong(genreIds[i]))
                            .name(genreNames[i])
                            .build());
                }
            }
        }
        return genreList;
    }

    private MPA mapRowToMpa(ResultSet resultSet) throws SQLException {
        return MPA.builder()
                .mpaRatingId(resultSet.getLong("MPA_RATING_ID"))
                .name(resultSet.getString("NAME"))
                .build();
    }


    private void deleteFilmGenresList(Long filmId) {
        String sqlQuery = "DELETE FROM FILM_GENRES " +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    private void updateFilmGenresList(Film film) {
        if (film.getGenres() == null) {
            return;
        }
        deleteFilmGenresList(film.getId());
        String sqlQuery = "INSERT INTO FILM_GENRES (FILM_ID,GENRE_ID) " +
                "VALUES(?,?)";
        Set<Genre> genres = new TreeSet<>(Comparator.comparingLong(Genre::getGenreId));
        genres.addAll(film.getGenres());
        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Genre genre = new ArrayList<>(genres).get(i);
                ps.setLong(1, film.getId());
                ps.setLong(2, genre.getGenreId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
    }


}

