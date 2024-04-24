package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import lombok.ToString;
import ru.yandex.practicum.filmorate.validator.DateIsAfter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
@ToString(callSuper = true)
public class Film {
    public static final String MIN_FILM_RELEASE_DATE= "1985-12-28";
    @Min(1)
    private Long id;
    @NotBlank
    private String name;
    @Size(min = 0, max = 200,message = "Максимум 200 символов")
    private String description;
    @DateIsAfter(value = MIN_FILM_RELEASE_DATE, message = "Дата не может быть раньше 28.12.1985" )
    private LocalDate releaseDate;
    @Min(1)
    private Long duration;
    private Set<Integer> likes = new HashSet<>();
    private Set<Genre> genres;
    private MPA mpaRating;

    public void addLikeFromUser(Integer id) {
        likes.add(id);
    }

    public void removeLikeFromUser(Integer id) {
        likes.remove(id);
    }

    public void setTestValue(List<Integer> nums) {
        likes.addAll(nums);
    }

}
