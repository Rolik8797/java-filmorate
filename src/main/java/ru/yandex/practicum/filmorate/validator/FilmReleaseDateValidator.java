package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.annotations.CorrectReleaseDay;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FilmReleaseDateValidator implements ConstraintValidator<CorrectReleaseDay, LocalDate> {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate.isAfter(MIN_RELEASE_DATE);
    }

    @Override
    public void initialize(CorrectReleaseDay constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}





