package io.github._0xorigin.flexscheduler.specifications.base;

import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskExecutionLogEntity;
import io.github._0xorigin.queryfilterbuilder.FilterContext;
import io.github._0xorigin.queryfilterbuilder.SortContext;

public interface ScheduledTaskExecutionLogSpecification {
    FilterContext.Template<ScheduledTaskExecutionLogEntity> getFilterTemplate();
    SortContext.Template<ScheduledTaskExecutionLogEntity> getSortTemplate();
}
