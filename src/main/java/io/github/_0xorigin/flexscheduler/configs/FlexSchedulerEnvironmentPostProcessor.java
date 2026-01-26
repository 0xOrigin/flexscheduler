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
     * @param environment The application's configurable environment.
     * @param application The Spring application.
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> defaultProperties = new LinkedHashMap<>();

        MapPropertySource propertySource = new MapPropertySource("flexscheduler", defaultProperties);
        environment.getPropertySources().addLast(propertySource);
    }
}
