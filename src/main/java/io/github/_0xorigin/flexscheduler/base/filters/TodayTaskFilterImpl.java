package io.github._0xorigin.flexscheduler.base.filters;

import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.base.filters.base.TodayTaskFilter;
import io.github._0xorigin.flexscheduler.base.repositories.ScheduledTaskRepository;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
public class TodayTaskFilterImpl implements TodayTaskFilter {

    private final ScheduledTaskRepository taskRepository;

    public TodayTaskFilterImpl(
        ScheduledTaskRepository taskRepository
    ) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<ScheduledTaskEntity> getAllTodayTasks() {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime startOfDay = getStartOfDay(now);
        OffsetDateTime endOfDay = getEndOfDay(now);
        return taskRepository.findAllActiveTasksInDateRange(startOfDay, endOfDay);
    }

    @Override
    public List<ScheduledTaskEntity> getAllTodayTasksExcludeSystemTasks(List<String> systemTaskTypes) {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime startOfDay = getStartOfDay(now);
        OffsetDateTime endOfDay = getEndOfDay(now);
        return taskRepository.findAllActiveTasksInDateRangeExcludeSystemTasks(startOfDay, endOfDay, systemTaskTypes);
    }

    @Override
    public boolean isNextExecutionTimeWithInToday(ScheduledTaskEntity task, OffsetDateTime nowDateTime) {
        OffsetDateTime startOfDay = getStartOfDay(nowDateTime);
        OffsetDateTime endOfDay = getEndOfDay(nowDateTime);
        if (task.getNextExecutionTime() == null)
            return false;

        OffsetDateTime next = task.getNextExecutionTime();
        boolean isAfterStartOfDay = next.isAfter(startOfDay);
        boolean isBeforeEndOfDay = next.isBefore(endOfDay);
        return isAfterStartOfDay && isBeforeEndOfDay;
    }

    private OffsetDateTime getStartOfDay(OffsetDateTime now) {
        return now.with(LocalTime.MIN);
    }

    private OffsetDateTime getEndOfDay(OffsetDateTime now) {
        return now.with(LocalTime.MAX);
    }
}
