package io.github._0xorigin.flexscheduler.dtos.responses;

import com.fasterxml.jackson.databind.JsonNode;
import io.github._0xorigin.flexscheduler.base.enums.ScheduledTaskExecutionStatus;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record ScheduledTaskExecutionLogListResponse(
   UUID id,
   OffsetDateTime startedAt,
   OffsetDateTime finishedAt,
   ScheduledTaskExecutionStatus status,
   JsonNode result
) {}
