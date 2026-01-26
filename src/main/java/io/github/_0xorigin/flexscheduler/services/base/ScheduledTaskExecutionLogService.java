package io.github._0xorigin.flexscheduler.services.base;

import io.github._0xorigin.flexscheduler.dtos.responses.ScheduledTaskExecutionLogListResponse;
import io.github._0xorigin.flexscheduler.dtos.responses.ScheduledTaskExecutionLogRetrieveResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface ScheduledTaskExecutionLogService {
    List<ScheduledTaskExecutionLogListResponse> list(HttpServletRequest httpServletRequest, UUID taskId);
    ScheduledTaskExecutionLogRetrieveResponse retrieve(UUID taskId, UUID id);
}
