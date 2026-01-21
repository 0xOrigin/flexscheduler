package io.github._0xorigin.flexscheduler.services;

import io.github._0xorigin.flexscheduler.base.dtos.CreateScheduledTaskRequest;
import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.base.repositories.ScheduledTaskRepository;
import io.github._0xorigin.flexscheduler.services.mappers.CreateToEntityMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduledTaskManagementService {

    private final List<CreateToEntityMapper> mappers;
    private final ScheduledTaskRepository taskRepository;

    /**
     * Map the incoming polymorphic DTO to the correct ScheduledTaskEntity and persist it.
     * Returns the saved entity instance.
     */
    @Transactional
    public ScheduledTaskEntity createAndSave(CreateScheduledTaskRequest request) {
        if (request == null) throw new IllegalArgumentException("request must not be null");

        CreateToEntityMapper mapper = mappers.stream()
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
