package io.github._0xorigin.flexscheduler.services.mappers;

import io.github._0xorigin.flexscheduler.base.dtos.CreateScheduledTaskRequest;
import io.github._0xorigin.flexscheduler.base.dtos.DateTimeScheduledTaskRequest;
import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.base.mappers.ScheduledTaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DateTimeCreateToEntityMapper implements CreateToEntityMapper {

    private final ScheduledTaskMapper mapper;

    @Override
    public boolean supports(Class<?> requestType) {
        return DateTimeScheduledTaskRequest.class.isAssignableFrom(requestType);
    }

    @Override
    public ScheduledTaskEntity toEntity(CreateScheduledTaskRequest request) {
        return mapper.dateTimeTaskRequestToEntity((DateTimeScheduledTaskRequest) request);
    }
}
