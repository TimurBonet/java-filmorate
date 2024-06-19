package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;


import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    public static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";  /*"^\\w+@[a-zA-Z0-9]+\\.[a-z]{2,4}";*/
    public static final String LOGIN_REGEX = "^\\S*$";
    @Min(1)
    private Integer id;
    @Email(regexp = EMAIL_REGEX, message = "некорректно введён email")
    private String email;
    @Pattern(regexp = LOGIN_REGEX, message = "Логин должен быть без пробелов")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();  // Забьем Id друзей


}
