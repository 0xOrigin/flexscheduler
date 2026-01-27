package io.github._0xorigin.flexscheduler.configs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A Spring {@link EnvironmentPostProcessor} that sets default configuration properties for the FlexScheduler package.
 */
public class FlexSchedulerEnvironmentPostProcessor implements EnvironmentPostProcessor {

    /**
     * Adds a {@link MapPropertySource} with default properties to the Spring environment.
     * This method is called by Spring Boot during the application startup process.
     *
     * We set `flexscheduler.liquibase.enabled=true` by default and also set
     * `spring.liquibase.enabled=false` as a default so that the host application's
     * Liquibase auto-configuration does not run and look for the application's
     * changelog (db.changelog-master.yaml). The host application can override
     * these defaults in its configuration.
     *
     * @param environment The application's configurable environment.
     * @param application The Spring application.
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> defaultProperties = new LinkedHashMap<>();
        defaultProperties.put("flexscheduler.liquibase.enabled", true);
        // Prevent the host app's default Liquibase auto-configuration from running
        // unless the host explicitly sets spring.liquibase.enabled.
//        defaultProperties.put("spring.liquibase.enabled", false);

        MapPropertySource propertySource = new MapPropertySource("flexscheduler", defaultProperties);
        environment.getPropertySources().addLast(propertySource);
    }
}
