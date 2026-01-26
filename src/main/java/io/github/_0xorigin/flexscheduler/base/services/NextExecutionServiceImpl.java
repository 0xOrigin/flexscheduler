package io.github._0xorigin.flexscheduler.base.services;

import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.base.services.base.NextExecutionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.CronExpression;

import java.time.Duration;
import java.time.OffsetDateTime;

@Slf4j
public class NextExecutionServiceImpl implements NextExecutionService {
    @Override
    public void computeAndSetNextExecutionFieldsOnTaskCreation(ScheduledTaskEntity task, OffsetDateTime now) {
        try {
            switch (task.getTypeOfExecution()) {
                case DATETIME -> setNextExecutionTimeForDateTimeTypeOnCreation(task);
                case CRON -> setNextExecutionTimeForCronTypeOnCreation(task, now);
                case START_TIME_AND_DURATION -> setNextExecutionTimeForStartTimeDurationTypeOnCreation(task, now);
                default -> task.setNextExecutionTime(null);
            }
        } catch (Exception e) {
            log.error("Failed to compute nextExecution fields for task: {}", task.getId(), e);
            task.setNextExecutionTime(null);
        }
    }

    @Override
    public void computeAndSetNextExecutionFieldsAfterTaskExecution(ScheduledTaskEntity task, OffsetDateTime now) {
        try {
            switch (task.getTypeOfExecution()) {
                case DATETIME -> setNextExecutionTimeForDateTimeTypeAfterExecution(task);
                case CRON -> setNextExecutionTimeForCronTypeAfterExecution(task, now);
                case START_TIME_AND_DURATION -> setNextExecutionTimeForStartTimeDurationTypeAfterExecution(task, now);
                default -> task.setNextExecutionTime(null);
            }
        } catch (Exception e) {
            log.error("Failed to compute nextExecution fields for task: {}", task.getId(), e);
            task.setNextExecutionTime(null);
        }
    }

    @Override
    public OffsetDateTime nextStartOccurrenceAfterOrEqual(OffsetDateTime start, Duration period, OffsetDateTime now) {
        if (!now.isAfter(start)) return start;

        long millisBetween = Duration.between(start, now).toMillis();
        long periodMillis = period.toMillis();
        long periodsPassed = millisBetween / periodMillis; // floor
        OffsetDateTime candidate = start.plus(Duration.ofMillis(periodsPassed * periodMillis));
        if (candidate.isBefore(now)) {
            candidate = candidate.plus(Duration.ofMillis(periodMillis));
        }
        return candidate;
    }

    private void setNextExecutionTimeForDateTimeTypeOnCreation(ScheduledTaskEntity task) {
        OffsetDateTime planned = task.getPlannedExecutionTime();
        task.setNextExecutionTime(planned);
    }

    private void setNextExecutionTimeForDateTimeTypeAfterExecution(ScheduledTaskEntity task) {
        task.setNextExecutionTime(null);
        task.setIsExecutionFinished(true);
    }

    private void setNextExecutionTimeForCronTypeOnCreation(ScheduledTaskEntity task, OffsetDateTime now) {
        CronExpression cron = CronExpression.parse(task.getCronExpression());
        OffsetDateTime next = cron.next(now);
        if (next == null) {
            task.setNextExecutionTime(null);
            task.setIsExecutionFinished(true);
        } else if (Boolean.TRUE.equals(task.getHasEnd()) && task.getEndExecutionTime() != null && next.isAfter(task.getEndExecutionTime())) {
            task.setNextExecutionTime(null);
            task.setIsExecutionFinished(true);
        } else {
            task.setNextExecutionTime(next);
        }
    }

    private void setNextExecutionTimeForStartTimeDurationTypeOnCreation(ScheduledTaskEntity task, OffsetDateTime now) {
        OffsetDateTime start = task.getStartDateTime();
        Duration period = task.getDuration();
        OffsetDateTime next = nextStartOccurrenceAfterOrEqual(start, period, now);
        if (next == null) {
            task.setNextExecutionTime(null);
            task.setIsExecutionFinished(true);
        } else if (Boolean.TRUE.equals(task.getHasEnd()) && task.getEndExecutionTime() != null && next.isAfter(task.getEndExecutionTime())) {
            task.setNextExecutionTime(null);
            task.setIsExecutionFinished(true);
        } else {
            task.setNextExecutionTime(next);
            task.setIsExecutionFinished(false);
        }
    }

    private void setNextExecutionTimeForCronTypeAfterExecution(ScheduledTaskEntity task, OffsetDateTime now) {
        CronExpression cron = CronExpression.parse(task.getCronExpression());
        OffsetDateTime next = cron.next(now);
        if (next == null) {
            task.setNextExecutionTime(null);
            task.setIsExecutionFinished(true);
        } else if (Boolean.TRUE.equals(task.getHasEnd()) && task.getEndExecutionTime() != null && next.isAfter(task.getEndExecutionTime())) {
            task.setNextExecutionTime(null);
            task.setIsExecutionFinished(true);
        } else {
            task.setNextExecutionTime(next);
        }
    }

    private void setNextExecutionTimeForStartTimeDurationTypeAfterExecution(ScheduledTaskEntity task, OffsetDateTime now) {
        OffsetDateTime start = task.getStartDateTime();
        Duration period = task.getDuration();
        OffsetDateTime next = nextStartOccurrenceAfterOrEqual(start, period, now);
        if (next == null) {
            task.setNextExecutionTime(null);
            task.setIsExecutionFinished(true);
        } else if (Boolean.TRUE.equals(task.getHasEnd()) && task.getEndExecutionTime() != null && next.isAfter(task.getEndExecutionTime())) {
            task.setNextExecutionTime(null);
            task.setIsExecutionFinished(true);
        } else {
            task.setNextExecutionTime(next);
            task.setIsExecutionFinished(false);
        }
    }
}
