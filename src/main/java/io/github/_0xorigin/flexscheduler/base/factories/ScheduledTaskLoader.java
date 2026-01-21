package io.github._0xorigin.flexscheduler.base.factories;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.base.factories.base.ScheduledTaskFactory;
import io.github._0xorigin.flexscheduler.operators.TaskSchedulerOperator;
import io.github._0xorigin.flexscheduler.utils.JsonNodeUtils;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class ScheduledTaskLoader implements ScheduledTaskFactory {

    public static final String TASK_TYPE = "Tasks24HoursLoader";
    private final ApplicationContext applicationContext;

    public ScheduledTaskLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public String getTaskType() {
        return TASK_TYPE;
    }

    @Override
    public JsonNode performTask(JsonNode arguments) {
        TaskSchedulerOperator taskSchedulerOperator = applicationContext.getBean(TaskSchedulerOperator.class);
        List<ScheduledTaskEntity> entityList = taskSchedulerOperator.getAllTodayTasksExcludeLoaders(
            List.of(getTaskType())
        );
        entityList.forEach(taskSchedulerOperator::scheduleTask);
        ObjectNode result = JsonNodeUtils.createObjectNode();
        result.put("status", "success");
        result.put("numberOfScheduledTasks", entityList.size());
        return result;
    }
}
