package io.github._0xorigin.flexscheduler.services;

import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.base.filters.base.TodayTaskFilter;
import io.github._0xorigin.flexscheduler.base.operators.base.TaskSchedulerOperator;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
public class TaskSchedulerService {

    private final TodayTaskFilter todayTaskFilter;
    private final TaskSchedulerOperator schedulerOperator;

    @Transactional
    public void scheduleTaskIfExecuteToday(ScheduledTaskEntity scheduledTask) {
        OffsetDateTime now = OffsetDateTime.now();
        scheduleTaskIfToday(scheduledTask, now);
    }

    @Transactional
    public void scheduleTasksIfExecuteToday(List<ScheduledTaskEntity> scheduledTasks) {
        OffsetDateTime now = OffsetDateTime.now();
        scheduledTasks.forEach(scheduledTask -> scheduleTaskIfToday(scheduledTask, now));
    }

    private void scheduleTaskIfToday(ScheduledTaskEntity scheduledTask, OffsetDateTime now) {
        if (
            !scheduledTask.getIsExecutionFinished()
            && (
                todayTaskFilter.isDateTimeTypeAndWithInToday(scheduledTask, now)
                || todayTaskFilter.isCronTypeAndWithInToday(scheduledTask, now)
                || todayTaskFilter.isStartDateTimeAndDurationAndWithInToday(scheduledTask, now)
            )
        )
            schedulerOperator.scheduleTask(scheduledTask);
    }
}
