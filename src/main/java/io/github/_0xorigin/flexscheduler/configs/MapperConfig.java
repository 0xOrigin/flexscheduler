package io.github._0xorigin.flexscheduler.configs;

import io.github._0xorigin.flexscheduler.base.factories.mappers.CronCreateToEntityMapper;
import io.github._0xorigin.flexscheduler.base.factories.mappers.DateTimeCreateToEntityMapper;
import io.github._0xorigin.flexscheduler.base.factories.mappers.StartTimeDurationCreateToEntityMapper;
import io.github._0xorigin.flexscheduler.base.mappers.ScheduledTaskMapper;
import io.github._0xorigin.flexscheduler.base.mappers.ScheduledTaskMapperImpl;
import org.springframework.context.annotation.Bean;

public class MapperConfig {
    @Bean ScheduledTaskMapper scheduledTaskMapper() {
        return new ScheduledTaskMapperImpl();
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
}
