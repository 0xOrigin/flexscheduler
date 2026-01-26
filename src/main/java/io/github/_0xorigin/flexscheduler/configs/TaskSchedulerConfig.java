package io.github._0xorigin.flexscheduler.configs;

import org.springframework.boot.task.SimpleAsyncTaskSchedulerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;

@Configuration
public class TaskSchedulerConfig {

    @Bean
    public TaskScheduler taskScheduler(SimpleAsyncTaskSchedulerBuilder builder) {
        return builder
            .threadNamePrefix("FlexScheduler-")
            .virtualThreads(true)
            .concurrencyLimit(1000)
            .build();
    }
}
