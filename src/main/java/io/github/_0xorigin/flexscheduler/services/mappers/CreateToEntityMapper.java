package io.github._0xorigin.flexscheduler.services.mappers;

import io.github._0xorigin.flexscheduler.base.dtos.CreateScheduledTaskRequest;
import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;

public interface CreateToEntityMapper {
    boolean supports(Class<?> requestType);
    ScheduledTaskEntity toEntity(CreateScheduledTaskRequest request);
}
