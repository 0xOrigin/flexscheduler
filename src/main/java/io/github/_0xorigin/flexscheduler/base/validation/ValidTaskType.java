package io.github._0xorigin.flexscheduler.base.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ValidTaskTypeValidator.class)
public @interface ValidTaskType {
    String message() default "Unknown taskType";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
