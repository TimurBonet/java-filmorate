package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@Data
public class User {
    @Min(1)
    private int id;
    @NonNull
    @Email
    private String email;
    @NonNull
    private String login;
    private String name;
    @NonNull
    private LocalDate birthday;
}
