package io.github._0xorigin.flexscheduler.base.factories.base;

import com.fasterxml.jackson.databind.JsonNode;

public interface ScheduledTaskFactory {
    String getTaskType();
    JsonNode performTask(JsonNode arguments);
}
