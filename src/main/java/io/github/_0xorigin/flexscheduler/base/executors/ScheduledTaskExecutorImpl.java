package io.github._0xorigin.flexscheduler.base.executors;

import com.fasterxml.jackson.databind.JsonNode;
import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskExecutionLogEntity;
import io.github._0xorigin.flexscheduler.base.enums.ScheduledTaskExecutionStatus;
import io.github._0xorigin.flexscheduler.base.enums.TaskExecutionType;
import io.github._0xorigin.flexscheduler.base.executors.base.ScheduledTaskExecutor;
import io.github._0xorigin.flexscheduler.base.factories.tasks.base.ScheduledTaskFactory;
import io.github._0xorigin.flexscheduler.base.repositories.ScheduledTaskExecutionLogRepository;
import io.github._0xorigin.flexscheduler.base.repositories.ScheduledTaskRepository;
import io.github._0xorigin.flexscheduler.utils.JsonNodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class ScheduledTaskExecutorImpl implements ScheduledTaskExecutor {
    private static final String TASK_ID_NOT_NULL_MESSAGE = "taskId must be not null";
    private final Logger log = LoggerFactory.getLogger("ScheduledTaskExecutor");
    private final ScheduledTaskRepository taskRepository;
    private final ScheduledTaskExecutionLogRepository logRepository;

    public ScheduledTaskExecutorImpl(
        ScheduledTaskRepository taskRepository,
        ScheduledTaskExecutionLogRepository logRepository
    ) {
        this.taskRepository = taskRepository;
        this.logRepository = logRepository;
    }

    @Override
    @Transactional
    public void executeTask(UUID taskId, ScheduledTaskFactory factory) {
        ScheduledTaskEntity task = getScheduledTaskInstance(taskId);

        if (isTaskInactive(task)) {
            log.info("Task [{}] - [{}] '{}' InActive - Skipping the execution", task.getId(), task.getTaskType(), task.getName());
            return;
        }

        ScheduledTaskExecutionLogEntity execution = createExecutionLogInstance(task);
        execution = saveExecutionLog(execution);

        logExecutionStart(task);

        try {
            JsonNode result = factory.performTask(task.getArguments());
            handleTaskSuccess(task, execution, result);
        } catch (Exception exception) {
            handleTaskFailure(task, execution, exception);
        } finally {
            finalizeTaskExecution(task, execution);
        }
    }

    private ScheduledTaskEntity getScheduledTaskInstance(UUID taskId) {
        Objects.requireNonNull(taskId, TASK_ID_NOT_NULL_MESSAGE);
        return taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
    }

    private boolean isTaskInactive(ScheduledTaskEntity task) {
        return Boolean.FALSE.equals(task.getIsActive());
    }

    private ScheduledTaskExecutionLogEntity createExecutionLogInstance(ScheduledTaskEntity task) {
        ScheduledTaskExecutionLogEntity execution = new ScheduledTaskExecutionLogEntity();
        execution.setTask(task);
        execution.setStatus(ScheduledTaskExecutionStatus.STARTED);
        execution.setStartedAt(OffsetDateTime.now());
        task.getExecutionLogs().add(execution);
        return execution;
    }

    private void handleTaskSuccess(ScheduledTaskEntity task, ScheduledTaskExecutionLogEntity execution, JsonNode result) {
        execution.setResult(result);
        execution.setStatus(ScheduledTaskExecutionStatus.SUCCESS);
        logExecutionSuccess(task);
    }

    private void handleTaskFailure(ScheduledTaskEntity task, ScheduledTaskExecutionLogEntity execution, Exception exception) {
        execution.setStatus(ScheduledTaskExecutionStatus.FAILED);
        execution.setResult(JsonNodeUtils.createObjectNode().put("error", exception.getMessage()));
        logExecutionFailure(task);
    }

    private void finalizeTaskExecution(ScheduledTaskEntity task, ScheduledTaskExecutionLogEntity execution) {
        execution.setFinishedAt(OffsetDateTime.now());
        saveExecutionLog(execution);
        if (task.getTypeOfExecution() == TaskExecutionType.DATETIME) {
            task.setIsExecutionFinished(true);
            saveTask(task);
        }
    }

    private ScheduledTaskEntity saveTask(ScheduledTaskEntity task) {
        return taskRepository.saveAndFlush(task);
    }

    private ScheduledTaskExecutionLogEntity saveExecutionLog(ScheduledTaskExecutionLogEntity execution) {
        return logRepository.saveAndFlush(execution);
    }

    private void logExecutionStart(ScheduledTaskEntity task) {
        log.info("Task [{}] - [{}] '{}' started", task.getId(), task.getTaskType(), task.getName());
    }

    private void logExecutionSuccess(ScheduledTaskEntity task) {
        log.info("Task [{}] - [{}] '{}' succeeded", task.getId(), task.getTaskType(), task.getName());
    }

    private void logExecutionFailure(ScheduledTaskEntity task) {
        log.info("Task [{}] - [{}] '{}' failed", task.getId(), task.getTaskType(), task.getName());
    }
}
