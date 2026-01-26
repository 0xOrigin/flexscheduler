package io.github._0xorigin.flexscheduler.base.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;
import io.github._0xorigin.flexscheduler.base.enums.TaskExecutionType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import io.github._0xorigin.flexscheduler.base.validation.ValidTaskType;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "typeOfExecution", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CronScheduledTaskRequest.class, name = "CRON"),
        @JsonSubTypes.Type(value = DateTimeScheduledTaskRequest.class, name = "DATETIME"),
        @JsonSubTypes.Type(value = StartTimeDurationScheduledTaskRequest.class, name = "START_TIME_AND_DURATION")
})
@ValidTaskType
public abstract class CreateScheduledTaskRequest {
    @NotBlank
    protected String name;
    @NotBlank
    protected String taskType;
    @NonNull
    protected TaskExecutionType typeOfExecution;
    protected JsonNode arguments;
    protected Boolean isActive;
    protected OffsetDateTime createdAt;
    protected String description;
    protected Boolean hasEnd;
    protected OffsetDateTime endExecutionTime;

    @AssertTrue(message = "endExecutionTime must be not null when hasEnd is true")
    public boolean isEndExecutionTimeValid() {
        if (hasEnd == null || !hasEnd || typeOfExecution == TaskExecutionType.DATETIME)
            return true;
        return endExecutionTime != null;
    }
}
