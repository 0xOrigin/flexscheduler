package io.github._0xorigin.flexscheduler.configs;

import io.github._0xorigin.flexscheduler.base.executors.ScheduledTaskExecutorImpl;
import io.github._0xorigin.flexscheduler.base.executors.base.ScheduledTaskExecutor;
import io.github._0xorigin.flexscheduler.base.factories.base.ScheduledTaskFactory;
import io.github._0xorigin.flexscheduler.base.mappers.ScheduledTaskMapper;
import io.github._0xorigin.flexscheduler.base.mappers.ScheduledTaskMapperImpl;
import io.github._0xorigin.flexscheduler.base.repositories.ScheduledTaskExecutionLogRepository;
import io.github._0xorigin.flexscheduler.base.repositories.ScheduledTaskRepository;
import io.github._0xorigin.flexscheduler.controllers.ScheduledTaskController;
import io.github._0xorigin.flexscheduler.operators.TaskSchedulerOperator;
import io.github._0xorigin.flexscheduler.services.ScheduledTaskManagementService;
import io.github._0xorigin.flexscheduler.services.TaskSchedulerService;
import io.github._0xorigin.flexscheduler.taskloaders.TaskLoaderRegistry;
import io.github._0xorigin.flexscheduler.base.factories.ScheduledTaskLoader;
import io.github._0xorigin.flexscheduler.services.mappers.CronCreateToEntityMapper;
import io.github._0xorigin.flexscheduler.services.mappers.DateTimeCreateToEntityMapper;
import io.github._0xorigin.flexscheduler.services.mappers.StartTimeDurationCreateToEntityMapper;
import io.github._0xorigin.flexscheduler.services.mappers.CreateToEntityMapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;

import java.util.List;

@AutoConfigureAfter(FlexSchedulerRepositoryAutoConfiguration.class)
public class FlexSchedulerServiceAutoConfiguration {

    @Bean ScheduledTaskMapper scheduledTaskMapper() {
        return new ScheduledTaskMapperImpl();
    }

    @Bean
    public ScheduledTaskExecutor scheduledTaskExecutor(
        ScheduledTaskRepository taskRepository,
        ScheduledTaskExecutionLogRepository logRepository
    ) {
        return new ScheduledTaskExecutorImpl(taskRepository, logRepository);
    }

    @Bean
    public ScheduledTaskController scheduledTaskController(
        TaskSchedulerService taskSchedulerService,
        ScheduledTaskRepository taskRepository,
        ScheduledTaskManagementService managementService
    ) {
        return new ScheduledTaskController(taskSchedulerService, taskRepository, managementService);
    }

    @Bean
    public CronCreateToEntityMapper cronCreateToEntityMapper(ScheduledTaskMapper scheduledTaskMapper) {
        return new CronCreateToEntityMapper(scheduledTaskMapper);
    }

    @Bean
    public DateTimeCreateToEntityMapper dateTimeCreateToEntityMapper(ScheduledTaskMapper scheduledTaskMapper) {
        return new DateTimeCreateToEntityMapper(scheduledTaskMapper);
    }

    @Bean
    public StartTimeDurationCreateToEntityMapper startTimeDurationCreateToEntityMapper(ScheduledTaskMapper scheduledTaskMapper) {
        return new StartTimeDurationCreateToEntityMapper(scheduledTaskMapper);
    }

    @Bean
    public ScheduledTaskLoader scheduledTaskLoader(ApplicationContext applicationContext) {
        return new ScheduledTaskLoader(applicationContext);
    }

    @Bean
    public TaskLoaderRegistry taskLoaderRegistry(
        ScheduledTaskRepository taskRepository,
        TaskSchedulerOperator taskSchedulerOperator,
        ScheduledTaskLoader scheduledTaskLoader
    ) {
        return new TaskLoaderRegistry(taskRepository, taskSchedulerOperator, scheduledTaskLoader);
    }

    @Bean
    public TaskSchedulerOperator taskSchedulerOperator(
        TaskScheduler scheduler,
        ScheduledTaskExecutor scheduledTaskExecutor,
        ScheduledTaskRepository taskRepository,
        List<ScheduledTaskFactory> factories
    ) {
        return new TaskSchedulerOperator(scheduler, scheduledTaskExecutor, taskRepository, factories);
    }

    @Bean
    public TaskSchedulerService taskSchedulerService(TaskSchedulerOperator taskSchedulerOperator) {
        return new TaskSchedulerService(taskSchedulerOperator);
    }

    @Bean
    public ScheduledTaskManagementService scheduledTaskManagementService(
        List<CreateToEntityMapper> mappers,
        ScheduledTaskRepository taskRepository
    ) {
        return new ScheduledTaskManagementService(mappers, taskRepository);
    }
}
