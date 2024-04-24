package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateAfterValidator  implements ConstraintValidator<DateIsAfter, LocalDate> {
    private LocalDate dateToCompare;

    @Override
    public void initialize(DateIsAfter constraintAnnotation) {
        this.dateToCompare = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate != null && localDate.isAfter(dateToCompare);
    }
}
