package io.github._0xorigin.flexscheduler.base.factories.mappers;

import io.github._0xorigin.flexscheduler.base.dtos.CreateScheduledTaskRequest;
import io.github._0xorigin.flexscheduler.base.dtos.DateTimeScheduledTaskRequest;
import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.base.factories.mappers.base.CreateToEntityMapperFactory;
import io.github._0xorigin.flexscheduler.base.mappers.ScheduledTaskMapper;

public class DateTimeCreateToEntityMapper implements CreateToEntityMapperFactory {

    private final ScheduledTaskMapper mapper;

    public DateTimeCreateToEntityMapper(ScheduledTaskMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public boolean supports(Class<?> requestType) {
        return DateTimeScheduledTaskRequest.class.isAssignableFrom(requestType);
    }

    @Override
    public ScheduledTaskEntity toEntity(CreateScheduledTaskRequest request) {
        return mapper.dateTimeTaskRequestToEntity((DateTimeScheduledTaskRequest) request);
    }
}
