package io.github._0xorigin.flexscheduler.configs;

import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskExecutionLogEntity;
import io.github._0xorigin.flexscheduler.base.executors.ScheduledTaskExecutorImpl;
import io.github._0xorigin.flexscheduler.base.executors.base.ScheduledTaskExecutor;
import io.github._0xorigin.flexscheduler.base.factories.tasks.base.ScheduledTaskFactory;
import io.github._0xorigin.flexscheduler.base.filters.TodayTaskFilterImpl;
import io.github._0xorigin.flexscheduler.base.filters.base.TodayTaskFilter;
import io.github._0xorigin.flexscheduler.base.mappers.ScheduledTaskExecutionLogMapper;
import io.github._0xorigin.flexscheduler.base.mappers.ScheduledTaskMapper;
import io.github._0xorigin.flexscheduler.base.repositories.ScheduledTaskExecutionLogRepository;
import io.github._0xorigin.flexscheduler.base.repositories.ScheduledTaskRepository;
import io.github._0xorigin.flexscheduler.base.services.NextExecutionServiceImpl;
import io.github._0xorigin.flexscheduler.base.services.base.NextExecutionService;
import io.github._0xorigin.flexscheduler.base.validation.ValidTaskTypeValidator;
import io.github._0xorigin.flexscheduler.controllers.ScheduledTaskController;
import io.github._0xorigin.flexscheduler.base.operators.TaskSchedulerOperatorImpl;
import io.github._0xorigin.flexscheduler.base.operators.base.TaskSchedulerOperator;
import io.github._0xorigin.flexscheduler.controllers.ScheduledTaskExecutionLogController;
import io.github._0xorigin.flexscheduler.services.ScheduledTaskExecutionLogServiceImpl;
import io.github._0xorigin.flexscheduler.services.TaskSchedulerServiceImpl;
import io.github._0xorigin.flexscheduler.services.base.ScheduledTaskExecutionLogService;
import io.github._0xorigin.flexscheduler.services.base.TaskSchedulerService;
import io.github._0xorigin.flexscheduler.base.registries.SystemTaskRegistry;
import io.github._0xorigin.flexscheduler.base.factories.mappers.base.CreateToEntityMapperFactory;
import io.github._0xorigin.flexscheduler.specifications.ScheduledTaskExecutionLogSpecificationImpl;
import io.github._0xorigin.flexscheduler.specifications.ScheduledTaskSpecificationImpl;
import io.github._0xorigin.flexscheduler.specifications.base.ScheduledTaskExecutionLogSpecification;
import io.github._0xorigin.flexscheduler.specifications.base.ScheduledTaskSpecification;
import io.github._0xorigin.queryfilterbuilder.QueryFilterBuilder;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.TaskScheduler;

import java.util.List;

@AutoConfigureAfter(FlexSchedulerRepositoryAutoConfiguration.class)
@Import({MapperConfig.class, SystemTaskConfig.class})
public class FlexSchedulerServiceAutoConfiguration {
    @Bean
    public NextExecutionService nextExecutionTimeService() {
        return new NextExecutionServiceImpl();
    }

    @Bean
    public ScheduledTaskSpecification scheduledTaskSpecification() {
        return new ScheduledTaskSpecificationImpl();
    }

    @Bean
    public TodayTaskFilter todayTaskFilter(
        ScheduledTaskRepository taskRepository
    ) {
        return new TodayTaskFilterImpl(taskRepository);
    }

    @Bean
    public TaskSchedulerOperator taskSchedulerOperator(
        TaskScheduler scheduler,
        ScheduledTaskExecutor scheduledTaskExecutor,
        TodayTaskFilter todayTaskFilter,
        List<ScheduledTaskFactory> factories
    ) {
        return new TaskSchedulerOperatorImpl(scheduler, scheduledTaskExecutor, todayTaskFilter, factories);
    }

    @Bean
    public TaskSchedulerService taskSchedulerService(
        List<CreateToEntityMapperFactory> mappers,
        ScheduledTaskRepository taskRepository,
        ScheduledTaskMapper taskMapper,
        ScheduledTaskSpecification scheduledTaskSpecification,
        QueryFilterBuilder<ScheduledTaskEntity> queryFilterBuilder,
        TodayTaskFilter todayTaskFilter,
        TaskSchedulerOperator taskSchedulerOperator,
        NextExecutionService nextExecutionService
    ) {
        return new TaskSchedulerServiceImpl(
            mappers,
            taskRepository,
            taskMapper,
            scheduledTaskSpecification,
            queryFilterBuilder,
            todayTaskFilter,
            taskSchedulerOperator,
            nextExecutionService
        );
    }

    @Bean
    public ScheduledTaskExecutor scheduledTaskExecutor(
        ScheduledTaskRepository taskRepository,
        ScheduledTaskExecutionLogRepository logRepository,
        NextExecutionService nextExecutionService
    ) {
        return new ScheduledTaskExecutorImpl(taskRepository, logRepository, nextExecutionService);
    }

    @Bean
    public SystemTaskRegistry systemTaskRegistry(
        ScheduledTaskRepository taskRepository,
        TaskSchedulerService taskSchedulerService,
        TaskSchedulerOperator taskSchedulerOperator
    ) {
        return new SystemTaskRegistry(taskRepository, taskSchedulerService, taskSchedulerOperator);
    }

    @Bean
    public ScheduledTaskController scheduledTaskController(
        TaskSchedulerService taskSchedulerService
    ) {
        return new ScheduledTaskController(taskSchedulerService);
    }

    @Bean
    public ValidTaskTypeValidator validTaskTypeValidator(TaskSchedulerOperator taskSchedulerOperator) {
        return new ValidTaskTypeValidator(taskSchedulerOperator);
    }

    @Bean
    public ScheduledTaskExecutionLogSpecification scheduledTaskExecutionLogSpecification() {
        return new ScheduledTaskExecutionLogSpecificationImpl();
    }

    @Bean
    public ScheduledTaskExecutionLogService scheduledTaskExecutionLogService(
        ScheduledTaskExecutionLogRepository logRepository,
        QueryFilterBuilder<ScheduledTaskExecutionLogEntity> queryFilterBuilder,
        ScheduledTaskExecutionLogSpecification logSpecification,
        ScheduledTaskExecutionLogMapper logMapper
    ) {
        return new ScheduledTaskExecutionLogServiceImpl(logRepository, queryFilterBuilder, logSpecification, logMapper);
    }

    @Bean
    public ScheduledTaskExecutionLogController scheduledTaskExecutionLogController(
        ScheduledTaskExecutionLogService logService
    ) {
        return  new ScheduledTaskExecutionLogController(logService);
    }
}
