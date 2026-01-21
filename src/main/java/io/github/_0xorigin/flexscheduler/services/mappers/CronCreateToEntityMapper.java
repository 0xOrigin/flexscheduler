package io.github._0xorigin.flexscheduler.services.mappers;

import io.github._0xorigin.flexscheduler.base.dtos.CreateScheduledTaskRequest;
import io.github._0xorigin.flexscheduler.base.dtos.CronScheduledTaskRequest;
import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.base.mappers.ScheduledTaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CronCreateToEntityMapper implements CreateToEntityMapper {

    private final ScheduledTaskMapper mapper;

    @Override
    public boolean supports(Class<?> requestType) {
        return CronScheduledTaskRequest.class.isAssignableFrom(requestType);
    }

    @Override
    public ScheduledTaskEntity toEntity(CreateScheduledTaskRequest request) {
        return mapper.cronTaskRequestToEntity((CronScheduledTaskRequest) request);
    }
}
