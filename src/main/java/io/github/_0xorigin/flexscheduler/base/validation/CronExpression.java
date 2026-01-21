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
@Constraint(validatedBy = {io.github._0xorigin.flexscheduler.base.validation.CronExpressionValidator.class})
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface CronExpression {
    String message() default "must be a valid cron expression (5-7 fields)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
