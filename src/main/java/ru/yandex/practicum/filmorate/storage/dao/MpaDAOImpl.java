package ru.yandex.practicum.filmorate.storage.dao;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MpaDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MpaDAOImpl implements MpaDAO {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<MPA> findById(Long id) {
        String sqlQuery = "SELECT *" +
                "FROM mpa " +
                "WHERE mpa_rating_id = ?";
        List<MPA> mpaList = jdbcTemplate.query(sqlQuery, this::mapRowToMpa, id);
        return mpaList.stream().findFirst();
    }

    @Override
    public List<MPA> findAll() {
        String sqlQuery = "SELECT * " +
                "FROM mpa ";

        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    private MPA mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return MPA.builder()
                .id(resultSet.getLong("mpa_rating_id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
