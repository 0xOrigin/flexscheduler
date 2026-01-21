package io.github._0xorigin.flexscheduler.base.validation;

import io.github._0xorigin.flexscheduler.base.dtos.CreateScheduledTaskRequest;
import io.github._0xorigin.flexscheduler.base.operators.base.TaskSchedulerOperator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidTaskTypeValidator implements ConstraintValidator<ValidTaskType, CreateScheduledTaskRequest> {

    private final TaskSchedulerOperator schedulerOperator;

    public ValidTaskTypeValidator(TaskSchedulerOperator schedulerOperator) {
        this.schedulerOperator = schedulerOperator;
    }

    @Override
    public void initialize(ValidTaskType constraintAnnotation) {
        // no-op
    }

    @Override
    public boolean isValid(CreateScheduledTaskRequest value, ConstraintValidatorContext context) {
        if (value == null)
            return true; // null-check handled elsewhere if desired
        String taskType = value.getTaskType();
        if (taskType == null)
            return true; // leave null/blank checks to @NotBlank on field

        boolean ok = schedulerOperator.getFactorySet().contains(taskType);
        if (!ok) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Unknown taskType: [" + taskType + "] not registered.")
                   .addPropertyNode("taskType")
                   .addConstraintViolation();
        }
        return ok;
    }
}
