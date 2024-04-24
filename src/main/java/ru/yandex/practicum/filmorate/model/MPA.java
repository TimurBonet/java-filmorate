package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MPA {
    private Long mpaRatingId;
    private String name;
}
