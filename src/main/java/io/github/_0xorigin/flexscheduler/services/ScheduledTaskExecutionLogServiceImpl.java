package io.github._0xorigin.flexscheduler.services;

import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskExecutionLogEntity;
import io.github._0xorigin.flexscheduler.base.mappers.ScheduledTaskExecutionLogMapper;
import io.github._0xorigin.flexscheduler.base.repositories.ScheduledTaskExecutionLogRepository;
import io.github._0xorigin.flexscheduler.dtos.responses.ScheduledTaskExecutionLogListResponse;
import io.github._0xorigin.flexscheduler.dtos.responses.ScheduledTaskExecutionLogRetrieveResponse;
import io.github._0xorigin.flexscheduler.services.base.ScheduledTaskExecutionLogService;
import io.github._0xorigin.flexscheduler.specifications.base.ScheduledTaskExecutionLogSpecification;
import io.github._0xorigin.queryfilterbuilder.FilterContext;
import io.github._0xorigin.queryfilterbuilder.QueryFilterBuilder;
import io.github._0xorigin.queryfilterbuilder.SortContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.rsocket.context.RSocketServerBootstrap;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public class ScheduledTaskExecutionLogServiceImpl implements ScheduledTaskExecutionLogService {
    private final ScheduledTaskExecutionLogRepository logRepository;
    private final QueryFilterBuilder<ScheduledTaskExecutionLogEntity> queryFilterBuilder;
    private final ScheduledTaskExecutionLogSpecification logSpecification;
    private final ScheduledTaskExecutionLogMapper logMapper;

    public ScheduledTaskExecutionLogServiceImpl(
        ScheduledTaskExecutionLogRepository logRepository,
        QueryFilterBuilder<ScheduledTaskExecutionLogEntity> queryFilterBuilder,
        ScheduledTaskExecutionLogSpecification logSpecification,
        ScheduledTaskExecutionLogMapper logMapper
    ) {
        this.logRepository = logRepository;
        this.queryFilterBuilder = queryFilterBuilder;
        this.logSpecification = logSpecification;
        this.logMapper = logMapper;
    }

    @Override
    public List<ScheduledTaskExecutionLogListResponse> list(HttpServletRequest httpServletRequest, UUID taskId) {
        Specification<ScheduledTaskExecutionLogEntity> specification = (root, cq, cb) -> cb.equal(root.get("task").get("id"), taskId);
        FilterContext<ScheduledTaskExecutionLogEntity> filterContext = logSpecification.getFilterTemplate()
            .newSourceBuilder()
            .withQuerySource(httpServletRequest)
            .buildFilterContext();
        SortContext<ScheduledTaskExecutionLogEntity> sortContext = logSpecification.getSortTemplate()
            .newSourceBuilder()
            .withQuerySource(httpServletRequest)
            .buildSortContext();
        specification = specification.and(queryFilterBuilder.buildFilterSpecification(filterContext));
        specification = specification.and(queryFilterBuilder.buildSortSpecification(sortContext));
        List<ScheduledTaskExecutionLogEntity> tasks = logRepository.findAll(specification);
        return logMapper.entitiesToListResponses(tasks);
    }

    @Override
    public ScheduledTaskExecutionLogRetrieveResponse retrieve(UUID taskId, UUID id) {
        ScheduledTaskExecutionLogEntity task = logRepository.findByIdAndTaskId(id, taskId).orElseThrow(() -> new RuntimeException("Execution Log not found"));
        return logMapper.entityToRetrieveResponse(task);
    }
}
