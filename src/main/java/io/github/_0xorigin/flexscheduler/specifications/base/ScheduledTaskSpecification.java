package io.github._0xorigin.flexscheduler.specifications.base;

import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.queryfilterbuilder.FilterContext;
import io.github._0xorigin.queryfilterbuilder.SortContext;

public interface ScheduledTaskSpecification {
    FilterContext.Template<ScheduledTaskEntity> getFilterTemplate();
    SortContext.Template<ScheduledTaskEntity> getSortTemplate();
}
