package io.github._0xorigin.flexscheduler.base.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {ValidCronExpressionValidator.class})
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface ValidCronExpression {
    String message() default "cronExpression must contain valid 6 fields";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
