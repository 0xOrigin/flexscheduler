package io.github._0xorigin.flexscheduler.services;

import io.github._0xorigin.flexscheduler.base.dtos.CreateScheduledTaskRequest;
import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.base.repositories.ScheduledTaskRepository;
import io.github._0xorigin.flexscheduler.base.factories.mappers.base.CreateToEntityMapperFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ScheduledTaskManagementService {

    private final List<CreateToEntityMapperFactory> mappers;
    private final ScheduledTaskRepository taskRepository;

    @Transactional
    public ScheduledTaskEntity createAndSave(CreateScheduledTaskRequest request) {
        if (request == null) throw new IllegalArgumentException("request must not be null");

        CreateToEntityMapperFactory mapper = mappers.stream()
                .filter(m -> m.supports(request.getClass()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No mapper found for " + request.getClass()));

        ScheduledTaskEntity taskEntity = mapper.toEntity(request);
        setDefaultFields(taskEntity);
        return taskRepository.saveAndFlush(taskEntity);
    }

    public void setDefaultFields(ScheduledTaskEntity taskEntity) {
        taskEntity.setIsExecutionFinished(false);
    }
}
