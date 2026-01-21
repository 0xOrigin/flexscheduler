package io.github._0xorigin.flexscheduler.taskloaders;

import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.base.repositories.ScheduledTaskRepository;
import io.github._0xorigin.flexscheduler.operators.TaskSchedulerOperator;
import io.github._0xorigin.flexscheduler.base.factories.ScheduledTaskLoader;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
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
