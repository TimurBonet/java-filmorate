package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MpaDAO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {
    private final MpaDAO mpaDAO;

    @Override
    public MPA findById(Long mpaId) {
        return mpaDAO.findById(mpaId)
                .orElseThrow(() -> {
                    throw new NotFoundException("Не найден id рейтинга", HttpStatus.NOT_FOUND);
                });
    }

    @Override
    public List<MPA> getAllMpa() {
        return mpaDAO.findAll();
    }
}
