package io.github._0xorigin.flexscheduler.configs;

import io.github._0xorigin.flexscheduler.base.factories.tasks.ScheduledTaskCleanup;
import io.github._0xorigin.flexscheduler.base.factories.tasks.ScheduledTaskLoader;
import io.github._0xorigin.flexscheduler.base.filters.base.TodayTaskFilter;
import io.github._0xorigin.flexscheduler.base.repositories.ScheduledTaskRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

public class SystemTaskConfig {
    @Bean
    public ScheduledTaskLoader scheduledTaskLoader(ApplicationContext applicationContext, TodayTaskFilter todayTaskFilter) {
        return new ScheduledTaskLoader(applicationContext, todayTaskFilter);
    }

    @Bean
    public ScheduledTaskCleanup scheduledTaskCleanup(ScheduledTaskRepository scheduledTaskRepository) {
        return new ScheduledTaskCleanup(scheduledTaskRepository);
    }
}
