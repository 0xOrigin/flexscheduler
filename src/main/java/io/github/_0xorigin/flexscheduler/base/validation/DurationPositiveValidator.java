package io.github._0xorigin.flexscheduler.base.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;

public class DurationPositiveValidator implements ConstraintValidator<DurationPositive, Duration> {

    @Override
    public boolean isValid(Duration value, ConstraintValidatorContext context) {
        if (value == null) return true;
        try {
            return !value.isZero() && value.compareTo(Duration.ZERO) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
