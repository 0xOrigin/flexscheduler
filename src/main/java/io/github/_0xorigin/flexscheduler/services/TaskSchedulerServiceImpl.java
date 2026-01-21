package io.github._0xorigin.flexscheduler.services;

import io.github._0xorigin.flexscheduler.base.dtos.CreateScheduledTaskRequest;
import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.base.factories.mappers.base.CreateToEntityMapperFactory;
import io.github._0xorigin.flexscheduler.base.filters.base.TodayTaskFilter;
import io.github._0xorigin.flexscheduler.base.operators.base.TaskSchedulerOperator;
import io.github._0xorigin.flexscheduler.base.repositories.ScheduledTaskRepository;
import io.github._0xorigin.flexscheduler.services.base.TaskSchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

public class TaskSchedulerServiceImpl implements TaskSchedulerService {
    private static final Logger log = LoggerFactory.getLogger("TaskSchedulerService");
    private final List<CreateToEntityMapperFactory> mappers;
    private final ScheduledTaskRepository taskRepository;
    private final TodayTaskFilter todayTaskFilter;
    private final TaskSchedulerOperator schedulerOperator;

    public TaskSchedulerServiceImpl(
        List<CreateToEntityMapperFactory> mappers,
        ScheduledTaskRepository taskRepository,
        TodayTaskFilter todayTaskFilter,
        TaskSchedulerOperator taskSchedulerOperator
    ) {
        this.mappers = mappers;
        this.taskRepository = taskRepository;
        this.todayTaskFilter = todayTaskFilter;
        this.schedulerOperator = taskSchedulerOperator;
    }

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

    @Transactional
    public ScheduledTaskEntity createAndSaveTask(CreateScheduledTaskRequest request) {
        if (request == null) {
            log.info("Task request is null");
            return null;
        }

        CreateToEntityMapperFactory mapper = mappers.stream()
            .filter(m -> m.supports(request.getClass()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No mapper found for " + request.getClass()));

        ScheduledTaskEntity taskEntity = mapper.toEntity(request);
        setDefaultFields(taskEntity);
        return taskRepository.saveAndFlush(taskEntity);
    }

    @Transactional
    public List<ScheduledTaskEntity> createAndSaveTasks(List<CreateScheduledTaskRequest> requests) {
        if (requests == null) {
            log.info("Task requests are null");
            return List.of();
        }

        List<ScheduledTaskEntity> tasks =  requests.stream()
            .map(request -> {
                CreateToEntityMapperFactory mapper = mappers.stream()
                    .filter(m -> m.supports(request.getClass()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No mapper found for " + request.getClass()));

                ScheduledTaskEntity task = mapper.toEntity(request);
                setDefaultFields(task);
                return task;
            })
            .toList();

        return taskRepository.saveAllAndFlush(tasks);
    }

    private void setDefaultFields(ScheduledTaskEntity task) {
        task.setIsExecutionFinished(false);
    }

    private void scheduleTaskIfToday(ScheduledTaskEntity task, OffsetDateTime now) {
        if (
            !task.getIsExecutionFinished()
            && (
                todayTaskFilter.isDateTimeTypeAndWithInToday(task, now)
                || todayTaskFilter.isCronTypeAndWithInToday(task, now)
                || todayTaskFilter.isStartDateTimeAndDurationAndWithInToday(task, now)
            )
        )
            schedulerOperator.scheduleTask(task);
    }
}
