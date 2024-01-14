package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@ToString(callSuper = true)
public class Film {
    @Min(1)
    private int id;
    @NotBlank
    private String name;
    @Size(min = 0, max = 200)
    private String description;
    private LocalDate releaseDate;
    @Min(1)
    private long duration;


}
