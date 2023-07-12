package ru.yandex.practicum.filmorate.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({ FIELD, PARAMETER, TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ReleaseDateValidator.class)

public @interface ReleaseDateValidation {
    String message() default "Дата релиза не может быть раньше 28 декабря 1895г. ";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
