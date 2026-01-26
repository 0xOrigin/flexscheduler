package io.github._0xorigin.flexscheduler.base.services;

import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.base.services.base.NextExecutionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.CronExpression;

import java.time.Duration;
import java.time.OffsetDateTime;

@Slf4j
public class NextExecutionServiceImpl implements NextExecutionService {

    private enum ExecutionPhase {
        ON_CREATION,
        AFTER_EXECUTION
    }

    @Override
    public void computeAndSetNextExecutionFieldsOnTaskCreation(ScheduledTaskEntity task, OffsetDateTime now) {
        computeAndSetNextExecutionFields(task, now, ExecutionPhase.ON_CREATION);
    }

    @Override
    public void computeAndSetNextExecutionFieldsAfterTaskExecution(ScheduledTaskEntity task, OffsetDateTime now) {
        computeAndSetNextExecutionFields(task, now, ExecutionPhase.AFTER_EXECUTION);
    }

    private void computeAndSetNextExecutionFields(ScheduledTaskEntity task, OffsetDateTime now, ExecutionPhase phase) {
        try {
            switch (task.getTypeOfExecution()) {
                case DATETIME -> computeNextExecutionForDateTimeType(task, phase);
                case CRON -> computeNextExecutionForCronType(task, now);
                case START_TIME_AND_DURATION -> computeNextExecutionForStartTimeAndDurationType(task, now);
                default -> task.setNextExecutionTime(null);
            }
        } catch (Exception e) {
            log.error("Failed to compute nextExecution fields for task: {}", task.getId(), e);
            task.setNextExecutionTime(null);
        }
    }

    @Override
    /**
     * Computes the next start occurrence for a fixed-duration schedule.
     *
     * The schedule is defined as: {@code start + n * period} for {@code n >= 0}.
     * This method returns the smallest occurrence {@code >= now}.
     *
     * Notes:
     * - Uses millisecond-based arithmetic ({@link Duration#toMillis()}) so the period is treated as a fixed duration,
     *   not a calendar-aware interval.
     * - Returns {@code null} for invalid inputs (null/zero/negative period) or if an overflow occurs.
     */
    public OffsetDateTime nextStartOccurrenceAfterOrEqual(OffsetDateTime start, Duration period, OffsetDateTime now) {
        if (start == null || period == null || now == null) return null;
        if (period.isZero() || period.isNegative()) return null;
        if (!now.isAfter(start)) return start;

        final long millisBetween;
        final long periodMillis;
        try {
            millisBetween = Duration.between(start, now).toMillis();
            periodMillis = period.toMillis();
        } catch (ArithmeticException e) {
            log.warn("Failed to compute duration in millis for next occurrence calculation", e);
            return null;
        }

        if (periodMillis <= 0) return null;

        // Floor division gives the latest occurrence that is <= now (may be strictly before now).
        long periodsPassed = millisBetween / periodMillis;
        final long deltaMillis;
        try {
            deltaMillis = Math.multiplyExact(periodsPassed, periodMillis);
        } catch (ArithmeticException e) {
            log.warn("Overflow while computing next occurrence delta", e);
            return null;
        }

        OffsetDateTime candidate;
        try {
            candidate = start.plus(Duration.ofMillis(deltaMillis));
        } catch (ArithmeticException e) {
            log.warn("Overflow while applying next occurrence delta", e);
            return null;
        }

        if (candidate.isBefore(now)) {
            // If we landed before 'now', advance exactly one period to make the result >= now.
            try {
                candidate = candidate.plus(Duration.ofMillis(periodMillis));
            } catch (ArithmeticException e) {
                log.warn("Overflow while advancing to next occurrence", e);
                return null;
            }
        }
        return candidate;
    }

    private void computeNextExecutionForDateTimeType(ScheduledTaskEntity task, ExecutionPhase phase) {
        if (phase == ExecutionPhase.AFTER_EXECUTION) {
            markTaskExecutionFinished(task);
            return;
        }

        OffsetDateTime planned = task.getPlannedExecutionTime();
        task.setNextExecutionTime(planned);
    }

    private void computeNextExecutionForCronType(ScheduledTaskEntity task, OffsetDateTime now) {
        CronExpression cron = CronExpression.parse(task.getCronExpression());
        OffsetDateTime next = cron.next(now);
        setNextExecutionOrMarkFinished(task, next);
    }

    private void computeNextExecutionForStartTimeAndDurationType(ScheduledTaskEntity task, OffsetDateTime now) {
        OffsetDateTime start = task.getStartDateTime();
        Duration period = task.getDuration();
        OffsetDateTime next = nextStartOccurrenceAfterOrEqual(start, period, now);

        if (next == null || isAfterEndExecutionTime(task, next)) {
            markTaskExecutionFinished(task);
            return;
        }

        task.setNextExecutionTime(next);
        task.setIsExecutionFinished(false);
    }

    private void setNextExecutionOrMarkFinished(ScheduledTaskEntity task, OffsetDateTime next) {
        if (next == null || isAfterEndExecutionTime(task, next)) {
            markTaskExecutionFinished(task);
            return;
        }

        task.setNextExecutionTime(next);
    }

    private boolean isAfterEndExecutionTime(ScheduledTaskEntity task, OffsetDateTime candidate) {
        return Boolean.TRUE.equals(task.getHasEnd())
                && task.getEndExecutionTime() != null
                && candidate != null
                && candidate.isAfter(task.getEndExecutionTime());
    }

    private void markTaskExecutionFinished(ScheduledTaskEntity task) {
        task.setNextExecutionTime(null);
        task.setIsExecutionFinished(true);
    }
}
