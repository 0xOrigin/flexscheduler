package io.github._0xorigin.flexscheduler.configs;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@AutoConfiguration(after = {LiquibaseAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})
@ConditionalOnClass(SpringLiquibase.class)
@ConditionalOnBean(DataSource.class)
public class FlexSchedulerLiquibaseAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "flexscheduler.liquibase", name = "enabled", havingValue = "true", matchIfMissing = true)
    public ApplicationRunner flexSchedulerLiquibase(DataSource dataSource) {
        return args -> {
            SpringLiquibase liquibase = new SpringLiquibase();
            liquibase.setDataSource(dataSource);
            liquibase.setDatabaseChangeLogTable("flexscheduler_databasechangelog");
            liquibase.setDatabaseChangeLogLockTable("flexscheduler_databasechangeloglock");
            liquibase.setChangeLog("classpath:db/changelog/db.changelog-flexscheduler.yaml");
            liquibase.setContexts("flexScheduler");
            liquibase.setBeanName("flexSchedulerLiquibase");
            liquibase.afterPropertiesSet();
        };
    }
}
