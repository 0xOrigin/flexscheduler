package io.github._0xorigin.flexscheduler.configs;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@AutoConfigureAfter({
    JpaRepositoriesAutoConfiguration.class,
    LiquibaseAutoConfiguration.class
})
public class FlexSchedulerLiquibaseAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "flexSchedulerLiquibase")
    @ConditionalOnProperty(prefix = "flexscheduler.liquibase", name = "enabled", matchIfMissing = true)
    public SpringLiquibase flexSchedulerLiquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setDatabaseChangeLogTable("flexscheduler_databasechangelog");
        liquibase.setDatabaseChangeLogLockTable("flexscheduler_databasechangeloglock");
        liquibase.setChangeLog("classpath:db/changelog/db.changelog-flexscheduler.yaml");
        liquibase.setContexts("flexScheduler");
        liquibase.setBeanName("flexSchedulerLiquibase");
        return liquibase;
    }
}
