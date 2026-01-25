package io.github._0xorigin.flexscheduler.services.base;

import io.github._0xorigin.flexscheduler.base.dtos.CreateScheduledTaskRequest;
import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.dtos.ScheduledTaskListResponse;
import io.github._0xorigin.flexscheduler.dtos.ScheduledTaskRetrieveResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface TaskSchedulerService {
    void scheduleTask(UUID id);
    void scheduleTaskIfExecuteToday(ScheduledTaskEntity scheduledTask);
    void scheduleTasksIfExecuteToday(List<ScheduledTaskEntity> scheduledTasks);
    ScheduledTaskEntity createTaskInstance(CreateScheduledTaskRequest request);
    ScheduledTaskRetrieveResponse createTask(CreateScheduledTaskRequest request);
    ScheduledTaskRetrieveResponse createTaskAndSchedule(CreateScheduledTaskRequest request);
    List<ScheduledTaskListResponse> createTasks(List<CreateScheduledTaskRequest> requests);
    List<ScheduledTaskListResponse> createTasksAndSchedule(List<CreateScheduledTaskRequest> requests);
    List<ScheduledTaskListResponse> list(HttpServletRequest httpServletRequest);
    ScheduledTaskRetrieveResponse retrieve(UUID id);
    void scheduleTaskIfExecuteToday(UUID id);
    void cancelTask(UUID id);
    void delete(UUID id);
}
