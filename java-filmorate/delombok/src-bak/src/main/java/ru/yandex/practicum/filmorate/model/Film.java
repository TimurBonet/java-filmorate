package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@ToString(callSuper = true)
public class Film {
    @Min(1)
    int id;
    @NotBlank
    String name;
    @Size(min = 0, max = 200)
    String description;
    LocalDate releaseDate;
    @Min(1)
    Duration duration;

}
