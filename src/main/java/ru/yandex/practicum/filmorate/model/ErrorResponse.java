package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class ErrorResponse {
    String error;
    String description;

    public ErrorResponse(String message, String er) {
        this.description = message;
        this.error = er;
    }
}
