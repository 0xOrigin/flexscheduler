package io.github._0xorigin.flexscheduler.base.executors.base;

import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.base.factories.tasks.base.ScheduledTaskFactory;

import java.util.Optional;
import java.util.UUID;

@FunctionalInterface
public interface ScheduledTaskExecutor {
    Optional<ScheduledTaskEntity> executeTask(UUID taskId, ScheduledTaskFactory factory);
}
