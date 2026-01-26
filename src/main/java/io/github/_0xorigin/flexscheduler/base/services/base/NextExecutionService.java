package io.github._0xorigin.flexscheduler.base.services.base;

import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;

import java.time.Duration;
import java.time.OffsetDateTime;

public interface NextExecutionService {
    void computeAndSetNextExecutionFieldsOnTaskCreation(ScheduledTaskEntity task, OffsetDateTime now);
    void computeAndSetNextExecutionFieldsAfterTaskExecution(ScheduledTaskEntity task, OffsetDateTime now);
    OffsetDateTime nextStartOccurrenceAfterOrEqual(OffsetDateTime start, Duration period, OffsetDateTime now);
}
