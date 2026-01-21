package io.github._0xorigin.flexscheduler.base.operators;

import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.base.executors.base.ScheduledTaskExecutor;
import io.github._0xorigin.flexscheduler.base.factories.tasks.base.ScheduledTaskFactory;
import io.github._0xorigin.flexscheduler.base.filters.base.TodayTaskFilter;
import io.github._0xorigin.flexscheduler.base.operators.base.TaskSchedulerOperator;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.scheduling.support.CronTrigger;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

public class TaskSchedulerOperatorImpl implements TaskSchedulerOperator {
    private final Logger log = LoggerFactory.getLogger("TaskSchedulerOperator");
    private final TaskScheduler scheduler;
    private final ScheduledTaskExecutor scheduledTaskExecutor;
    private final TodayTaskFilter todayTaskFilter;
    private final Map<String, ScheduledTaskFactory> factoryTypes = new ConcurrentHashMap<>();
    private final Map<UUID, ScheduledFuture<?>> scheduledFutures = new ConcurrentHashMap<>();

    public TaskSchedulerOperatorImpl(
        TaskScheduler scheduler,
        ScheduledTaskExecutor scheduledTaskExecutor,
        TodayTaskFilter todayTaskFilter,
        List<ScheduledTaskFactory> factories
    ) {
        this.scheduler = scheduler;
        this.scheduledTaskExecutor = scheduledTaskExecutor;
        this.todayTaskFilter = todayTaskFilter;
        registerFactories(factories);
    }

    @Override
    @PostConstruct
    public void init() {
        log.info("Registered task types: {}", factoryTypes.keySet());
        scheduleAllActiveTasks();
    }

    @Override
    public Set<String> getFactorySet() {
        return Collections.unmodifiableSet(factoryTypes.keySet());
    }

    @Override
    public void scheduleAllActiveTasks() {
        List<ScheduledTaskEntity> tasks = todayTaskFilter.getAllTodayTasks();
        tasks.forEach(this::scheduleTask);
    }

    @Override
    public void scheduleTask(final ScheduledTaskEntity task) {
        final ScheduledTaskFactory factory = factoryTypes.get(task.getTaskType());
        if (factory == null) {
            log.error("No factory for taskType '{}', skipping task '{}'", task.getTaskType(), task.getName());
            return;
        }

        Runnable runnable = createRunnableInstance(task, factory);

        scheduledFutures.compute(task.getId(), (id, prev) -> computeAndScheduleFuture(id, prev, task, runnable));
    }

    private ScheduledFuture<?> computeAndScheduleFuture(UUID taskId, ScheduledFuture<?> prev, ScheduledTaskEntity task, Runnable runnable) {
        cancelPreviousFutureIfExists(prev, taskId);
        try {
            ScheduledFuture<?> future = createFutureForTask(task, runnable);
            if (future == null) {
                log.warn("Failed to schedule task [{}] - '{}', skipping task '{}'", task.getId(), task.getName(), task.getName());
                return prev;
            }

            logScheduledTask(task);
            return future;
        } catch (Exception e) {
            log.error("Failed to schedule task [{}] - '{}' (type: {})", task.getId(), task.getName(), task.getTaskType(), e);
            return prev;
        }
    }

    @Override
    public void cancelTask(UUID taskId) {
        scheduledFutures.computeIfPresent(taskId, (id, future) -> {
            try {
                future.cancel(false);
            } catch (Exception e) {
                log.warn("Failed to cancel task {}", id, e);
            }
            log.info("Cancelled scheduled task ID: {}", taskId);
            return null;
        });
    }

    @Override
    @PreDestroy
    public void shutdown() {
        log.info("Shutting down the Scheduler.........");
        scheduledFutures.values().forEach(future -> future.cancel(false));
    }

    private Runnable createRunnableInstance(final ScheduledTaskEntity task, final ScheduledTaskFactory factory) {
       return () -> scheduledTaskExecutor.executeTask(task.getId(), factory);
    }

    private void cancelPreviousFutureIfExists(ScheduledFuture<?> prev, UUID taskId) {
        if (prev == null || prev.isDone())
            return;
        try {
            prev.cancel(false);
        } catch (Exception e) {
            log.warn("Failed to cancel previous future for task {}", taskId, e);
        }
    }

    private ScheduledFuture<?> createFutureForTask(ScheduledTaskEntity task, Runnable runnable) {
        return switch (task.getTypeOfExecution()) {
            case CRON -> scheduler.schedule(runnable, new CronTrigger(task.getCronExpression(), ZoneId.systemDefault()));
            case DATETIME -> scheduler.schedule(runnable, task.getPlannedExecutionTime().toInstant());
            case START_TIME_AND_DURATION -> scheduler.scheduleAtFixedRate(
                    runnable,
                    task.getStartDateTime().toInstant(),
                    task.getDuration()
            );
        };
    }

    private void logScheduledTask(ScheduledTaskEntity task) {
        switch (task.getTypeOfExecution()) {
            case CRON -> {
                CronExpression cron = CronExpression.parse(task.getCronExpression());
                log.info("Scheduled task [{}] - '{}', (type: {}), next run at: {}", task.getId(), task.getName(), task.getTaskType(), cron.next(OffsetDateTime.now()));
            }
            case DATETIME -> log.info("Scheduled task [{}] - '{}' (type: {}), at: {}", task.getId(), task.getName(), task.getTaskType(), task.getPlannedExecutionTime());
            case START_TIME_AND_DURATION -> log.info("Scheduled task [{}] - '{}' (type: {})", task.getId(), task.getName(), task.getTaskType());
        }
    }

    private void registerFactories(List<ScheduledTaskFactory> factories) {
        if (factories == null || factories.isEmpty()) {
            log.info("No factories provided");
            return;
        }
        factories.forEach(factory -> {
            String taskType = factory.getTaskType();
            ScheduledTaskFactory existing = factoryTypes.putIfAbsent(taskType, factory);
            if (existing != null) {
                String msg = String.format(
                    "Duplicate ScheduledTaskFactory for taskType ['%s']: [%s] (new) conflicts with [%s] (existing)",
                    taskType,
                    factory.getClass().getName(),
                    existing.getClass().getName()
                );
                throw new IllegalArgumentException(msg);
            }
        });
    }
}
