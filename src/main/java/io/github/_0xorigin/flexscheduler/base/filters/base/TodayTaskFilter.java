package io.github._0xorigin.flexscheduler.base.filters.base;

import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;

import java.time.OffsetDateTime;
import java.util.List;

public interface TodayTaskFilter {
    List<ScheduledTaskEntity> getAllTodayTasks();
    List<ScheduledTaskEntity> getAllTodayTasksExcludeLoaders(List<String> loaders);
    boolean isDateTimeTypeAndWithInToday(ScheduledTaskEntity taskEntity, OffsetDateTime nowDateTime);
    boolean isCronTypeAndWithInToday(ScheduledTaskEntity taskEntity, OffsetDateTime nowDateTime);
    boolean isStartDateTimeAndDurationAndWithInToday(ScheduledTaskEntity taskEntity, OffsetDateTime nowDateTime);
}
