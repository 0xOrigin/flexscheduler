package io.github._0xorigin.flexscheduler.configs;

import io.github._0xorigin.flexscheduler.base.repositories.ScheduledTaskExecutionLogRepository;
import io.github._0xorigin.flexscheduler.base.repositories.ScheduledTaskRepository;
import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

@AutoConfigureAfter(HibernateJpaAutoConfiguration.class)
public class FlexSchedulerRepositoryAutoConfiguration {

    @Bean
    public ScheduledTaskRepository scheduledTaskRepository(EntityManager entityManager) {
        return new JpaRepositoryFactory(entityManager).getRepository(ScheduledTaskRepository.class);
    }

    @Bean
    public ScheduledTaskExecutionLogRepository scheduledTaskExecutionLogRepository(EntityManager entityManager) {
        return new JpaRepositoryFactory(entityManager).getRepository(ScheduledTaskExecutionLogRepository.class);
    }
}
