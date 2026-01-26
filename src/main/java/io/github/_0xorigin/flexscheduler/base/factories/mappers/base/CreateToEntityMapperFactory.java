package io.github._0xorigin.flexscheduler.base.factories.mappers.base;

import io.github._0xorigin.flexscheduler.base.dtos.CreateScheduledTaskRequest;
import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;

public interface CreateToEntityMapperFactory {
    boolean supports(Class<?> requestType);
    ScheduledTaskEntity toEntity(CreateScheduledTaskRequest request);
}
