package io.github._0xorigin.flexscheduler.base.registries;

import io.github._0xorigin.flexscheduler.base.dtos.CronScheduledTaskRequest;
import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.base.factories.tasks.ScheduledTaskCleanup;
import io.github._0xorigin.flexscheduler.base.factories.tasks.ScheduledTaskLoader;
import io.github._0xorigin.flexscheduler.base.repositories.ScheduledTaskRepository;
import io.github._0xorigin.flexscheduler.base.operators.base.TaskSchedulerOperator;
import io.github._0xorigin.flexscheduler.services.base.TaskSchedulerService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class SystemTaskRegistry {
    private final ScheduledTaskRepository scheduledTaskRepository;
    private final TaskSchedulerService taskSchedulerService;
    private final TaskSchedulerOperator taskSchedulerOperator;

    public SystemTaskRegistry(
        ScheduledTaskRepository taskRepository,
        TaskSchedulerService taskSchedulerService,
        TaskSchedulerOperator taskSchedulerOperator
    ) {
        this.scheduledTaskRepository = taskRepository;
        this.taskSchedulerService = taskSchedulerService;
        this.taskSchedulerOperator = taskSchedulerOperator;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        List<String> systemTaskTypes = List.of(ScheduledTaskLoader.TASK_TYPE, ScheduledTaskCleanup.TASK_TYPE);

        systemTaskTypes.forEach(systemTaskType -> {
            List<ScheduledTaskEntity> systemTasks = scheduledTaskRepository.findAllByTaskType(systemTaskType);
            createTaskLoaderIfNotExists(systemTaskType, systemTasks);
            createTaskCleanupIfNotExists(systemTaskType, systemTasks);
            scheduleIfExists(systemTasks);
        });
    }

    public void createTaskLoaderIfNotExists(String taskType, List<ScheduledTaskEntity> systemTasks) {
        if (!taskType.equals(ScheduledTaskLoader.TASK_TYPE) || !systemTasks.isEmpty())
            return;

        CronScheduledTaskRequest scheduledTaskRequest = CronScheduledTaskRequest.builder()
                .name("FlexScheduler TaskLoader")
                .taskType(ScheduledTaskLoader.TASK_TYPE)
                .cronExpression("0 0 0 * * *") // Every day at midnight
                .isActive(true)
                .hasEnd(false)
                .build();
        ScheduledTaskEntity task = taskSchedulerService.createTaskInstance(scheduledTaskRequest);
        taskSchedulerOperator.scheduleTask(task);
    }

    public void createTaskCleanupIfNotExists(String taskType, List<ScheduledTaskEntity> systemTasks) {
        if (!taskType.equals(ScheduledTaskCleanup.TASK_TYPE) || !systemTasks.isEmpty())
            return;

        CronScheduledTaskRequest scheduledTaskRequest = CronScheduledTaskRequest.builder()
            .name("FlexScheduler TaskCleanup")
            .taskType(ScheduledTaskCleanup.TASK_TYPE)
            .cronExpression("0 0 0 1 * ?") // Every first day of the month at midnight
            .isActive(true)
            .hasEnd(false)
            .build();
        ScheduledTaskEntity task = taskSchedulerService.createTaskInstance(scheduledTaskRequest);
        taskSchedulerOperator.scheduleTask(task);
    }

    public void scheduleIfExists(List<ScheduledTaskEntity> systemTasks) {
        if (systemTasks.isEmpty())
            return;

        systemTasks.forEach(taskSchedulerOperator::scheduleTask);
    }
}
