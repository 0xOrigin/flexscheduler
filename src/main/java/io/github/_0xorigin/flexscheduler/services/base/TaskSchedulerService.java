package io.github._0xorigin.flexscheduler.services.base;

import io.github._0xorigin.flexscheduler.base.dtos.CreateScheduledTaskRequest;
import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;

import java.util.List;

public interface TaskSchedulerService {
    void scheduleTaskIfExecuteToday(ScheduledTaskEntity scheduledTask);
    void scheduleTasksIfExecuteToday(List<ScheduledTaskEntity> scheduledTasks);
    ScheduledTaskEntity createAndSaveTask(CreateScheduledTaskRequest request);
    List<ScheduledTaskEntity> createAndSaveTasks(List<CreateScheduledTaskRequest> requests);
}
