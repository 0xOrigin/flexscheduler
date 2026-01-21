package io.github._0xorigin.flexscheduler.base.filters;

import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.base.filters.base.TodayTaskFilter;
import io.github._0xorigin.flexscheduler.base.repositories.ScheduledTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.CronExpression;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static io.github._0xorigin.flexscheduler.base.enums.TaskExecutionType.*;

@Slf4j
public class TodayTaskFilterImpl implements TodayTaskFilter {

    private final ScheduledTaskRepository taskRepository;

    public TodayTaskFilterImpl(ScheduledTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<ScheduledTaskEntity> getAllTodayTasks() {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime startOfDay = getStartOfDay(now);
        OffsetDateTime endOfDay = getEndOfDay(now);
        return taskRepository.findAllActiveTasksInDateRange(startOfDay, endOfDay)
            .stream()
            .filter(task -> {
                if (task.getTypeOfExecution() != CRON)
                    return true;
                return isCronTypeAndWithInToday(task, now);
            })
            .toList();
    }

    @Override
    public List<ScheduledTaskEntity> getAllTodayTasksExcludeLoaders(List<String> loaders) {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime startOfDay = getStartOfDay(now);
        OffsetDateTime endOfDay = getEndOfDay(now);
        return taskRepository.findAllActiveTasksInDateRangeExcludeLoaders(startOfDay, endOfDay, loaders)
            .stream()
            .filter(task -> {
                if (task.getTypeOfExecution() != CRON)
                    return true;
                return isCronTypeAndWithInToday(task, now);
            })
            .toList();
    }

    @Override
    public boolean isDateTimeTypeAndWithInToday(ScheduledTaskEntity taskEntity, OffsetDateTime nowDateTime) {
        OffsetDateTime startOfDay = getStartOfDay(nowDateTime);
        OffsetDateTime endOfDay = getEndOfDay(nowDateTime);
        boolean isDateTimeType = taskEntity.getTypeOfExecution() == DATETIME;
        if (!isDateTimeType)
            return false;
        boolean isAfterStartOfDay = taskEntity.getPlannedExecutionTime().isAfter(startOfDay);
        boolean isBeforeEndOfDay = taskEntity.getPlannedExecutionTime().isBefore(endOfDay);
        return isAfterStartOfDay && isBeforeEndOfDay;
    }

    @Override
    public boolean isCronTypeAndWithInToday(ScheduledTaskEntity taskEntity, OffsetDateTime nowDateTime) {
        OffsetDateTime endOfDay = getEndOfDay(nowDateTime);
        boolean isCronType = taskEntity.getTypeOfExecution() == CRON;
        if (!isCronType)
            return false;
        try {
            CronExpression cron = CronExpression.parse(taskEntity.getCronExpression());
            return Optional.ofNullable(cron.next(nowDateTime))
                .map(dateTime -> !dateTime.isAfter(endOfDay))
                .orElse(false);
        } catch (Exception e) {
            log.error("Invalid cron expression: " + taskEntity.getCronExpression(), e);
            return false;
        }
    }

    @Override
    public boolean isStartDateTimeAndDurationAndWithInToday(ScheduledTaskEntity taskEntity, OffsetDateTime nowDateTime) {
        OffsetDateTime endOfDay = getEndOfDay(nowDateTime);
        boolean isStartTimeAndDurationType = taskEntity.getTypeOfExecution() == START_TIME_AND_DURATION;
        if (!isStartTimeAndDurationType)
            return false;
        return taskEntity.getStartDateTime().isBefore(endOfDay);
    }

    private OffsetDateTime getStartOfDay(OffsetDateTime now) {
        return now.with(LocalTime.MIN);
    }

    private OffsetDateTime getEndOfDay(OffsetDateTime now) {
        return now.with(LocalTime.MAX);
    }
}
