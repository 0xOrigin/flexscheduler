package io.github._0xorigin.flexscheduler.base.operators.base;

import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;

import java.util.Set;
import java.util.UUID;

public interface TaskSchedulerOperator {
    void init();
    Set<String> getFactorySet();
    void scheduleAllActiveTasks();
    void scheduleTask(final ScheduledTaskEntity task);
    void cancelTask(UUID taskId);
    void shutdown();
}
