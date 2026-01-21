package io.github._0xorigin.flexscheduler.base.factories.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.base.factories.tasks.base.ScheduledTaskFactory;
import io.github._0xorigin.flexscheduler.base.filters.base.TodayTaskFilter;
import io.github._0xorigin.flexscheduler.base.operators.base.TaskSchedulerOperator;
import io.github._0xorigin.flexscheduler.utils.JsonNodeUtils;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class ScheduledTaskLoader implements ScheduledTaskFactory {

    public static final String TASK_TYPE = "SYS-ScheduledTaskLoader";
    private final ApplicationContext applicationContext;
    private final TodayTaskFilter todayTaskFilter;

    public ScheduledTaskLoader(ApplicationContext applicationContext, TodayTaskFilter todayTaskFilter) {
        this.applicationContext = applicationContext;
        this.todayTaskFilter = todayTaskFilter;
    }

    @Override
    public String getTaskType() {
        return TASK_TYPE;
    }

    @Override
    public JsonNode performTask(JsonNode arguments) {
        TaskSchedulerOperator taskSchedulerOperator = applicationContext.getBean(TaskSchedulerOperator.class);
        List<ScheduledTaskEntity> tasks = todayTaskFilter.getAllTodayTasksExcludeSystemTasks(
            List.of(TASK_TYPE, ScheduledTaskCleanup.TASK_TYPE)
        );
        tasks.forEach(taskSchedulerOperator::scheduleTask);
        ObjectNode result = JsonNodeUtils.createObjectNode();
        result.put("status", "success");
        result.put("numberOfScheduledTasks", tasks.size());
        return result;
    }
}
