package io.github._0xorigin.flexscheduler.base.executors.base;

import io.github._0xorigin.flexscheduler.base.factories.base.ScheduledTaskFactory;

import java.util.UUID;

@FunctionalInterface
public interface ScheduledTaskExecutor {
    void executeTask(UUID taskId, ScheduledTaskFactory factory);
}
