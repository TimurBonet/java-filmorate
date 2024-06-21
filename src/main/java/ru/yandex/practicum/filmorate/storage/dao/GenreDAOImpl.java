package ru.yandex.practicum.filmorate.storage.dao;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreDAOImpl implements GenreDAO {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Genre> findGenreById(Long id) {
        String sqlQuery = " SELECT * " +
                "FROM genre " +
                "WHERE genre_id = ?";
        List<Genre> genresList = jdbcTemplate.query(sqlQuery, this::mapRowToGenre, id);
        return genresList.stream().findFirst();
    }

    @Override
    public List<Genre> findAllGenres() {
        String sqlQuery = "SELECT * " +
                "FROM genre ";

        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    @Override
    public boolean genreIsExistById(Long id) {
        String sqlQuery = "SELECT EXISTS(SELECT 1 FROM genre WHERE genre_id = ?)";
        return jdbcTemplate.queryForObject(sqlQuery, Boolean.class, id);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getLong("genre_id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
