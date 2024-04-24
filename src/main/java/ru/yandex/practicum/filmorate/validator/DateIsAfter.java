package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DateAfterValidator.class)
public @interface DateIsAfter {
    String message() default "Дата не может быть ранее {value}";
    Class<?> [] groups() default {};
    String value() default "1970-01-01";
    Class <? extends Payload>[] payLoad() default {};
}
