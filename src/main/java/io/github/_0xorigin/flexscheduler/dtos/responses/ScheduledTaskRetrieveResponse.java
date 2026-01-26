package io.github._0xorigin.flexscheduler.dtos.responses;

import com.fasterxml.jackson.databind.JsonNode;
import io.github._0xorigin.flexscheduler.base.enums.TaskExecutionType;
import lombok.Builder;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record ScheduledTaskRetrieveResponse(
    UUID id,
    String name,
    String description,
    String taskType,
    TaskExecutionType typeOfExecution,
    String cronExpression,
    OffsetDateTime plannedExecutionTime,
    OffsetDateTime startDateTime,
    Duration duration,
    OffsetDateTime nextExecutionTime,
    JsonNode arguments,
    Boolean isActive,
    Boolean isExecutionFinished,
    Boolean hasEnd,
    OffsetDateTime endExecutionTime,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    String claimedBy,
    OffsetDateTime lastClaimedAt,
    Long version
) {}
