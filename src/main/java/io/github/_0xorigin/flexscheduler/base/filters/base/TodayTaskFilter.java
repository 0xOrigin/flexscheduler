package io.github._0xorigin.flexscheduler.base.filters.base;

import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;

import java.time.OffsetDateTime;
import java.util.List;

public interface TodayTaskFilter {
    List<ScheduledTaskEntity> getAllTodayTasks();
    List<ScheduledTaskEntity> getAllTodayTasksExcludeSystemTasks(List<String> loaders);
    boolean isNextExecutionTimeWithInToday(ScheduledTaskEntity task, OffsetDateTime nowDateTime);
}
