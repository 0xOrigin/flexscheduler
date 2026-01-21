package io.github._0xorigin.flexscheduler.configs;

import io.github._0xorigin.flexscheduler.base.executors.ScheduledTaskExecutorImpl;
import io.github._0xorigin.flexscheduler.base.executors.base.ScheduledTaskExecutor;
import io.github._0xorigin.flexscheduler.base.factories.tasks.ScheduledTaskCleanup;
import io.github._0xorigin.flexscheduler.base.factories.tasks.base.ScheduledTaskFactory;
import io.github._0xorigin.flexscheduler.base.filters.TodayTaskFilterImpl;
import io.github._0xorigin.flexscheduler.base.filters.base.TodayTaskFilter;
import io.github._0xorigin.flexscheduler.base.mappers.ScheduledTaskMapper;
import io.github._0xorigin.flexscheduler.base.mappers.ScheduledTaskMapperImpl;
import io.github._0xorigin.flexscheduler.base.repositories.ScheduledTaskExecutionLogRepository;
import io.github._0xorigin.flexscheduler.base.repositories.ScheduledTaskRepository;
import io.github._0xorigin.flexscheduler.controllers.ScheduledTaskController;
import io.github._0xorigin.flexscheduler.base.operators.TaskSchedulerOperatorImpl;
import io.github._0xorigin.flexscheduler.base.operators.base.TaskSchedulerOperator;
import io.github._0xorigin.flexscheduler.services.ScheduledTaskManagementService;
import io.github._0xorigin.flexscheduler.services.TaskSchedulerService;
import io.github._0xorigin.flexscheduler.systemtaskregistries.TaskLoaderRegistry;
import io.github._0xorigin.flexscheduler.base.factories.tasks.ScheduledTaskLoader;
import io.github._0xorigin.flexscheduler.base.factories.mappers.CronCreateToEntityMapper;
import io.github._0xorigin.flexscheduler.base.factories.mappers.DateTimeCreateToEntityMapper;
import io.github._0xorigin.flexscheduler.base.factories.mappers.StartTimeDurationCreateToEntityMapper;
import io.github._0xorigin.flexscheduler.base.factories.mappers.base.CreateToEntityMapperFactory;
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
    public TodayTaskFilter todayTaskFilter(ScheduledTaskRepository taskRepository) {
        return new TodayTaskFilterImpl(taskRepository);
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
    public ScheduledTaskLoader scheduledTaskLoader(ApplicationContext applicationContext, TodayTaskFilter todayTaskFilter) {
        return new ScheduledTaskLoader(applicationContext, todayTaskFilter);
    }

    @Bean
    public ScheduledTaskCleanup scheduledTaskCleanup(ScheduledTaskRepository scheduledTaskRepository) {
        return new ScheduledTaskCleanup(scheduledTaskRepository);
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
        TodayTaskFilter todayTaskFilter,
        List<ScheduledTaskFactory> factories
    ) {
        return new TaskSchedulerOperatorImpl(scheduler, scheduledTaskExecutor, todayTaskFilter, factories);
    }

    @Bean
    public TaskSchedulerService taskSchedulerService(TodayTaskFilter todayTaskFilter, TaskSchedulerOperator taskSchedulerOperator) {
        return new TaskSchedulerService(todayTaskFilter, taskSchedulerOperator);
    }

    @Bean
    public ScheduledTaskManagementService scheduledTaskManagementService(
        List<CreateToEntityMapperFactory> mappers,
        ScheduledTaskRepository taskRepository
    ) {
        return new ScheduledTaskManagementService(mappers, taskRepository);
    }
}
