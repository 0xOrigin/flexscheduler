package io.github._0xorigin.flexscheduler.base.factories.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github._0xorigin.flexscheduler.base.factories.tasks.base.ScheduledTaskFactory;
import io.github._0xorigin.flexscheduler.base.repositories.ScheduledTaskRepository;
import io.github._0xorigin.flexscheduler.utils.JsonNodeUtils;

public class ScheduledTaskCleanup implements ScheduledTaskFactory {

    public static final String TASK_TYPE = "SYS-ScheduledTaskCleanup";
    private final ScheduledTaskRepository scheduledTaskRepository;

    public ScheduledTaskCleanup(ScheduledTaskRepository scheduledTaskRepository) {
        this.scheduledTaskRepository = scheduledTaskRepository;
    }

    @Override
    public String getTaskType() {
        return TASK_TYPE;
    }

    @Override
    public JsonNode performTask(JsonNode arguments) {
        long count = scheduledTaskRepository.countByIsExecutionFinishedTrue();
        if (count > 0) {
            scheduledTaskRepository.deleteByIsExecutionFinishedTrue();
        }
        ObjectNode result = JsonNodeUtils.createObjectNode();
        result.put("status", "success");
        result.put("numberOfDeletedTasks", count);
        return result;
    }
}
