package io.github._0xorigin.flexscheduler.configs;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfigureBefore({
    HibernateJpaAutoConfiguration.class,
    JpaRepositoriesAutoConfiguration.class
})
@Import(FlexSchedulerPackageRegistrar.class)
public class FlexSchedulerPackageAutoConfiguration {
}
