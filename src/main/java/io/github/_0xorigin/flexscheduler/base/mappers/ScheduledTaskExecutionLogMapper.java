package io.github._0xorigin.flexscheduler.base.mappers;

import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskExecutionLogEntity;
import io.github._0xorigin.flexscheduler.dtos.responses.ScheduledTaskExecutionLogListResponse;
import io.github._0xorigin.flexscheduler.dtos.responses.ScheduledTaskExecutionLogRetrieveResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScheduledTaskExecutionLogMapper {
    ScheduledTaskExecutionLogListResponse entityToListResponse(ScheduledTaskExecutionLogEntity entity);

    List<ScheduledTaskExecutionLogListResponse> entitiesToListResponses(List<ScheduledTaskExecutionLogEntity> entities);

    ScheduledTaskExecutionLogRetrieveResponse entityToRetrieveResponse(ScheduledTaskExecutionLogEntity entity);
}
