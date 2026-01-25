package io.github._0xorigin.flexscheduler.services;

import io.github._0xorigin.flexscheduler.base.dtos.CreateScheduledTaskRequest;
import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.base.factories.mappers.base.CreateToEntityMapperFactory;
import io.github._0xorigin.flexscheduler.base.filters.base.TodayTaskFilter;
import io.github._0xorigin.flexscheduler.base.mappers.ScheduledTaskMapper;
import io.github._0xorigin.flexscheduler.base.operators.base.TaskSchedulerOperator;
import io.github._0xorigin.flexscheduler.base.repositories.ScheduledTaskRepository;
import io.github._0xorigin.flexscheduler.dtos.ScheduledTaskListResponse;
import io.github._0xorigin.flexscheduler.dtos.ScheduledTaskRetrieveResponse;
import io.github._0xorigin.flexscheduler.services.base.TaskSchedulerService;
import io.github._0xorigin.flexscheduler.specifications.base.ScheduledTaskSpecification;
import io.github._0xorigin.queryfilterbuilder.FilterContext;
import io.github._0xorigin.queryfilterbuilder.QueryFilterBuilder;
import io.github._0xorigin.queryfilterbuilder.SortContext;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class TaskSchedulerServiceImpl implements TaskSchedulerService {
    private static final Logger log = LoggerFactory.getLogger("TaskSchedulerService");
    private final List<CreateToEntityMapperFactory> mappers;
    private final ScheduledTaskRepository taskRepository;
    private final ScheduledTaskMapper taskMapper;
    private final ScheduledTaskSpecification scheduledTaskSpecification;
    private final QueryFilterBuilder<ScheduledTaskEntity> queryFilterBuilder;
    private final TodayTaskFilter todayTaskFilter;
    private final TaskSchedulerOperator schedulerOperator;

    public TaskSchedulerServiceImpl(
        List<CreateToEntityMapperFactory> mappers,
        ScheduledTaskRepository taskRepository,
        ScheduledTaskMapper taskMapper,
        ScheduledTaskSpecification scheduledTaskSpecification,
        QueryFilterBuilder<ScheduledTaskEntity> queryFilterBuilder,
        TodayTaskFilter todayTaskFilter,
        TaskSchedulerOperator taskSchedulerOperator
    ) {
        this.mappers = mappers;
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.scheduledTaskSpecification = scheduledTaskSpecification;
        this.queryFilterBuilder = queryFilterBuilder;
        this.todayTaskFilter = todayTaskFilter;
        this.schedulerOperator = taskSchedulerOperator;
    }

    @Override
    public void scheduleTaskIfExecuteToday(ScheduledTaskEntity scheduledTask) {
        OffsetDateTime now = OffsetDateTime.now();
        scheduleTaskIfToday(scheduledTask, now);
    }

    @Override
    public void scheduleTasksIfExecuteToday(List<ScheduledTaskEntity> scheduledTasks) {
        OffsetDateTime now = OffsetDateTime.now();
        scheduledTasks.forEach(scheduledTask -> scheduleTaskIfToday(scheduledTask, now));
    }

    @Override
    public ScheduledTaskEntity createTaskInstance(CreateScheduledTaskRequest request) {
        if (request == null) {
            log.info("Task request is null");
            return null;
        }

        ScheduledTaskEntity taskEntity = mapRequestToEntityAndSetDefaults(request);
        return taskRepository.saveAndFlush(taskEntity);
    }

    @Override
    @Transactional
    public ScheduledTaskRetrieveResponse createTask(CreateScheduledTaskRequest request) {
        ScheduledTaskEntity taskEntity = createTaskInstance(request);
        return taskMapper.entityToRetrieveResponse(taskEntity);
    }

    @Override
    @Transactional
    public ScheduledTaskRetrieveResponse createTaskAndSchedule(CreateScheduledTaskRequest request) {
        ScheduledTaskEntity taskEntity = createTaskInstance(request);
        scheduleTaskIfExecuteToday(taskEntity);
        return taskMapper.entityToRetrieveResponse(taskEntity);
    }

    @Override
    @Transactional
    public List<ScheduledTaskListResponse> createTasks(List<CreateScheduledTaskRequest> requests) {
        List<ScheduledTaskEntity> tasks = createTasksInstances(requests);
        return taskMapper.entitiesToListResponses(tasks);
    }

    @Override
    @Transactional
    public List<ScheduledTaskListResponse> createTasksAndSchedule(List<CreateScheduledTaskRequest> requests) {
        List<ScheduledTaskEntity> tasks = createTasksInstances(requests);
        scheduleTasksIfExecuteToday(tasks);
        return taskMapper.entitiesToListResponses(tasks);
    }

    @Override
    public List<ScheduledTaskListResponse> list(HttpServletRequest httpServletRequest) {
        FilterContext<ScheduledTaskEntity> filterContext = scheduledTaskSpecification.getFilterTemplate()
                .newSourceBuilder()
                .withQuerySource(httpServletRequest)
                .buildFilterContext();
        SortContext<ScheduledTaskEntity> sortContext = scheduledTaskSpecification.getSortTemplate()
                .newSourceBuilder()
                .withQuerySource(httpServletRequest)
                .buildSortContext();
        Specification<ScheduledTaskEntity> specification = queryFilterBuilder.buildFilterSpecification(filterContext);
        specification = specification.and(queryFilterBuilder.buildSortSpecification(sortContext));
        List<ScheduledTaskEntity> tasks = taskRepository.findAll(specification);
        return taskMapper.entitiesToListResponses(tasks);
    }

    @Override
    public ScheduledTaskRetrieveResponse retrieve(UUID id) {
        ScheduledTaskEntity task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        return taskMapper.entityToRetrieveResponse(task);
    }

    @Override
    public void scheduleTaskIfExecuteToday(UUID id) {
        ScheduledTaskEntity scheduledTask = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        OffsetDateTime now = OffsetDateTime.now();
        scheduleTaskIfToday(scheduledTask, now);
    }

    @Override
    public void scheduleTask(UUID id) {
        ScheduledTaskEntity scheduledTask = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        schedulerOperator.scheduleTask(scheduledTask);
    }

    @Override
    public void cancelTask(UUID id) {
        schedulerOperator.cancelTask(id);
    }

    @Override
    public void delete(UUID id) {
        ScheduledTaskEntity task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
    }

    private void setDefaultFields(ScheduledTaskEntity task) {
        task.setIsExecutionFinished(false);
    }

    private void scheduleTaskIfToday(ScheduledTaskEntity task, OffsetDateTime now) {
        if (
            Boolean.FALSE.equals(task.getIsExecutionFinished())
            && (
                todayTaskFilter.isDateTimeTypeAndWithInToday(task, now)
                || todayTaskFilter.isCronTypeAndWithInToday(task, now)
                || todayTaskFilter.isStartDateTimeAndDurationAndWithInToday(task, now)
            )
        )
            schedulerOperator.scheduleTask(task);
    }

    private List<ScheduledTaskEntity> createTasksInstances(List<CreateScheduledTaskRequest> requests) {
        if (requests == null) {
            log.info("Task requests are null");
            return List.of();
        }

        List<ScheduledTaskEntity> tasks = requests.stream()
                .map(this::mapRequestToEntityAndSetDefaults)
                .toList();
        return taskRepository.saveAllAndFlush(tasks);
    }

    private CreateToEntityMapperFactory getMapper(Class<?> clazz) {
        return mappers.stream()
                .filter(m -> m.supports(clazz))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No mapper found for " + clazz));
    }

    private ScheduledTaskEntity mapRequestToEntityAndSetDefaults(CreateScheduledTaskRequest request) {
        CreateToEntityMapperFactory mapper = getMapper(request.getClass());
        ScheduledTaskEntity taskEntity = mapper.toEntity(request);
        setDefaultFields(taskEntity);
        return taskEntity;
    }
}
