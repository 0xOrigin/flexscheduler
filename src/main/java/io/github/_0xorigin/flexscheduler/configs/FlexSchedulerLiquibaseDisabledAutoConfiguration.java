package io.github._0xorigin.flexscheduler.configs;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * When flexscheduler.liquibase.enabled=false we register a no-op "liquibase" bean
 * so that Spring Boot's LiquibaseAutoConfiguration will not attempt to create and
 * run a real SpringLiquibase instance (which fails when the application has no changelog).
 */
@AutoConfigureBefore(LiquibaseAutoConfiguration.class)
@ConditionalOnProperty(prefix = "flexscheduler.liquibase", name = "enabled", havingValue = "false")
public class FlexSchedulerLiquibaseDisabledAutoConfiguration {

    @Bean(name = "liquibase")
    @ConditionalOnMissingBean(name = "liquibase")
    @ConditionalOnProperty(prefix = "spring.liquibase", name = "enabled", havingValue = "false")
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        // Ensure Liquibase does not run if the host application doesn't provide changelogs
        liquibase.setShouldRun(false);
        return liquibase;
    }
}
