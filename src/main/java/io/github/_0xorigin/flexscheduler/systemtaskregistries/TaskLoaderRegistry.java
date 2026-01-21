package io.github._0xorigin.flexscheduler.systemtaskregistries;

import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.base.repositories.ScheduledTaskRepository;
import io.github._0xorigin.flexscheduler.base.factories.tasks.ScheduledTaskLoader;
import io.github._0xorigin.flexscheduler.base.operators.base.TaskSchedulerOperator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
public class TaskLoaderRegistry {

    private final ScheduledTaskRepository scheduledTaskRepository;
    private final TaskSchedulerOperator taskSchedulerOperator;
    private final ScheduledTaskLoader scheduledTaskLoader;

    @PostConstruct
    @Transactional
    public void init() {
        List<ScheduledTaskEntity> loaderTasks = scheduledTaskRepository.findAllByTaskType(scheduledTaskLoader.getTaskType());
        createIfNotExists(loaderTasks);
        scheduleIfExists(loaderTasks);
    }

    public void createIfNotExists(List<ScheduledTaskEntity> loaderTasks) {
        if (!loaderTasks.isEmpty())
            return;

//        CronScheduledTaskRequest scheduledTaskRequest = CronScheduledTaskRequest.builder()
//                .name("TaskLoader - " + LocalDate.now())
//                .cronExpression("0 0 0 * * *") // midnight every day
//                .build();
//        ScheduledTaskEntity task = taskLoaderJob.createTask(scheduledTaskRequest);
//        taskSchedulerOperator.scheduleTask(task);
    }

    public void scheduleIfExists(List<ScheduledTaskEntity> loaderTasks) {
        if (loaderTasks.isEmpty())
            return;

        loaderTasks.forEach(taskSchedulerOperator::scheduleTask);
    }
}
