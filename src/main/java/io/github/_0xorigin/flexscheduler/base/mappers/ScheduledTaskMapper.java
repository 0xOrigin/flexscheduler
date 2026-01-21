package io.github._0xorigin.flexscheduler.base.mappers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import io.github._0xorigin.flexscheduler.base.dtos.CronScheduledTaskRequest;
import io.github._0xorigin.flexscheduler.base.dtos.DateTimeScheduledTaskRequest;
import io.github._0xorigin.flexscheduler.base.dtos.StartTimeDurationScheduledTaskRequest;
import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScheduledTaskMapper {
    @Mapping(target = "arguments", source = "arguments", qualifiedByName = "jsonNodeHandleNull")
    ScheduledTaskEntity cronTaskRequestToEntity(CronScheduledTaskRequest scheduledTaskRequest);

    @Mapping(target = "arguments", source = "arguments", qualifiedByName = "jsonNodeHandleNull")
    ScheduledTaskEntity dateTimeTaskRequestToEntity(DateTimeScheduledTaskRequest scheduledTaskRequest);

    @Mapping(target = "arguments", source = "arguments", qualifiedByName = "jsonNodeHandleNull")
    ScheduledTaskEntity startTimeDurationTaskRequestToEntity(StartTimeDurationScheduledTaskRequest scheduledTaskRequest);

    List<ScheduledTaskEntity> cronTaskRequestsToEntities(List<CronScheduledTaskRequest> scheduledTaskRequests);

    List<ScheduledTaskEntity> dateTimeTaskRequestsToEntities(List<DateTimeScheduledTaskRequest> scheduledTaskRequests);

    List<ScheduledTaskEntity> startTimeDurationTaskRequestsToEntities(List<StartTimeDurationScheduledTaskRequest> scheduledTaskRequests);

    @Mapping(target = "arguments", source = "arguments", qualifiedByName = "jsonNodeHandleNull")
    CronScheduledTaskRequest entityToCronTaskRequest(ScheduledTaskEntity scheduledTaskEntity);

    @Mapping(target = "arguments", source = "arguments", qualifiedByName = "jsonNodeHandleNull")
    DateTimeScheduledTaskRequest entityToDateTimeTaskRequest(ScheduledTaskEntity scheduledTaskEntity);

    @Mapping(target = "arguments", source = "arguments", qualifiedByName = "jsonNodeHandleNull")
    StartTimeDurationScheduledTaskRequest entityToStartTimeDurationTaskRequest(ScheduledTaskEntity scheduledTaskEntity);

    List<CronScheduledTaskRequest> entitiesToCronTaskRequests(List<ScheduledTaskEntity> entityList);

    List<DateTimeScheduledTaskRequest> entitiesToDateTimeTaskRequests(List<ScheduledTaskEntity> entityList);

    List<StartTimeDurationScheduledTaskRequest> entitiesToStartTimeDurationTaskRequests(List<ScheduledTaskEntity> entityList);

    @Named("jsonNodeHandleNull")
    default JsonNode jsonNodeHandleNull(JsonNode arguments) {
        return arguments == null ? JsonNodeFactory.instance.objectNode() : arguments;
    }
}
