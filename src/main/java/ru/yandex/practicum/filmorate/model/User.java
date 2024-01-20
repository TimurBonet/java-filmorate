package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    @Min(1)
    private Integer id;
    @NonNull
    @Email
    private String email;
    @NonNull
    private String login;
    private String name;
    @NonNull
    private LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();  // Забьем Id друзей

    public void addIdUserFriend (Integer id) {
        friends.add(id);
    }

    public void removeIdUserFriend(Integer removingId) {
        friends.remove(removingId);
    }


}
