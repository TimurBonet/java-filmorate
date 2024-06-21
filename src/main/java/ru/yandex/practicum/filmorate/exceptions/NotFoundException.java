package ru.yandex.practicum.filmorate.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@Builder

public class NotFoundException extends RuntimeException {
    private String message;
    private HttpStatus status;
}
