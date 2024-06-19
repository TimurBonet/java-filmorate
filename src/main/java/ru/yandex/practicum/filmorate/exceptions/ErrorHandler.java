package ru.yandex.practicum.filmorate.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice(annotations = RestController.class)
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<Error> handleValidException(final ValidateException e) {
        return new ResponseEntity<>(new Error("Ошибка: " + e.getMessage()), e.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<Error> handleNotFoundException(final NotFoundException e) {
        return new ResponseEntity<>(new Error("Ошибка: " + e.getMessage()), e.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<Error> handleNotFoundException(final MethodArgumentNotValidException e) {
        return new ResponseEntity<>(new Error("Ошибка: " + e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}

/*

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleFilmAlreadyExist(final FilmAlreadyExistException e) {
        return new ErrorResponse("Такой фильм уже есть в каталоге.", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidFilmData(final InvalidFilmDataException e) {
        return new ErrorResponse("Некорректные данные фильма! \nПроверьте соответствие критериям: " +
                "\n название не может быть пустым;\n" +
                "максимальная длина описания — " + maxChars + " символов;\n" +
                "дата релиза — не раньше " + minReleaseDate + ";\n" +
                "продолжительность фильма должна быть положительной.", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidEmail(final InvalidEmailException e) {
        return new ErrorResponse("Отсутствует или неверно задан email! \nПроверьте по критериям: \n" +
                "электронная почта не может быть пустой и должна содержать символ @;\n" +
                "логин не может быть пустым и содержать пробелы;\n" +
                "дата рождения не может быть в будущем.", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUnknownUserUpdate(final UnknownUserUpdateException e) {
        return new ErrorResponse("Нет такого пользователя в списке, проверьте правильность запроса.",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserAlreadyExist(final UserAlreadyExistException e) {
        return new ErrorResponse("Такой пользователь уже существует",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(final UserNotFoundException e) {
        return new ErrorResponse("Такой пользователь не найден",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotFound(final FilmNotFoundException e) {
        return new ErrorResponse("Такой фильм не найден",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotLikedException(final NotLikedException e) {
        return new ErrorResponse("Невозможно убрать лайк",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final NotLikedException e) {
        return new ErrorResponse("Невозможно убрать лайк",
                e.getMessage()
        );
    }
*/



