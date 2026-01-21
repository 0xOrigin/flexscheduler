package io.github._0xorigin.flexscheduler.base.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// Lightweight validator: basic syntax check (5 or 6+ fields). For production, consider cron-utils.
public class CronExpressionValidator implements ConstraintValidator<CronExpression, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            // null allowed; presence may be enforced by ExecutionTypeConsistency where required
            return true;
        }
        String[] parts = value.trim().split("\\s+");
        return parts.length >= 5 && parts.length <= 7 && containsNoEmpty(parts);
    }

    private boolean containsNoEmpty(String[] parts) {
        for (String p : parts) {
            if (p == null || p.isBlank()) return false;
        }
        return true;
    }
}
