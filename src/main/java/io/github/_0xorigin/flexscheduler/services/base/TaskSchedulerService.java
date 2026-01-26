package io.github._0xorigin.flexscheduler.services.base;

import io.github._0xorigin.flexscheduler.base.dtos.CreateScheduledTaskRequest;
import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.dtos.ScheduledTaskListResponse;
import io.github._0xorigin.flexscheduler.dtos.ScheduledTaskRetrieveResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Validated
public interface TaskSchedulerService {
    void scheduleTask(UUID id);
    void scheduleTaskIfExecuteToday(ScheduledTaskEntity scheduledTask);
    void scheduleTasksIfExecuteToday(List<ScheduledTaskEntity> scheduledTasks);
    ScheduledTaskEntity createTaskInstance(@Valid CreateScheduledTaskRequest request);
    ScheduledTaskRetrieveResponse createTask(@Valid CreateScheduledTaskRequest request);
    ScheduledTaskRetrieveResponse createTaskAndSchedule(@Valid CreateScheduledTaskRequest request);
    List<ScheduledTaskListResponse> createTasks(List<@Valid CreateScheduledTaskRequest> requests);
    List<ScheduledTaskListResponse> createTasksAndSchedule(List<@Valid CreateScheduledTaskRequest> requests);
    List<ScheduledTaskListResponse> list(HttpServletRequest httpServletRequest);
    ScheduledTaskRetrieveResponse retrieve(UUID id);
    void scheduleTaskIfExecuteToday(UUID id);
    void cancelTask(UUID id);
    void delete(UUID id);
}
