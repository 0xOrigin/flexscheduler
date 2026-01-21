package io.github._0xorigin.flexscheduler.services;

import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.operators.TaskSchedulerOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TaskSchedulerService {

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
                schedulerOperator.isDateTimeTypeAndWithInToday(scheduledTask, now)
                || schedulerOperator.isCronTypeAndWithInToday(scheduledTask, now)
                || schedulerOperator.isStartDateTimeAndDurationAndWithInToday(scheduledTask, now)
            )
        )
            schedulerOperator.scheduleTask(scheduledTask);
    }
}
