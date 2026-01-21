package io.github._0xorigin.flexscheduler.base.factories.mappers;

import io.github._0xorigin.flexscheduler.base.dtos.CreateScheduledTaskRequest;
import io.github._0xorigin.flexscheduler.base.dtos.CronScheduledTaskRequest;
import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.base.factories.mappers.base.CreateToEntityMapperFactory;
import io.github._0xorigin.flexscheduler.base.mappers.ScheduledTaskMapper;

public class CronCreateToEntityMapper implements CreateToEntityMapperFactory {

    private final ScheduledTaskMapper mapper;

    public CronCreateToEntityMapper(ScheduledTaskMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public boolean supports(Class<?> requestType) {
        return CronScheduledTaskRequest.class.isAssignableFrom(requestType);
    }

    @Override
    public ScheduledTaskEntity toEntity(CreateScheduledTaskRequest request) {
        return mapper.cronTaskRequestToEntity((CronScheduledTaskRequest) request);
    }
}
