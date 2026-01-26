package io.github._0xorigin.flexscheduler.specifications;

import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskExecutionLogEntity;
import io.github._0xorigin.flexscheduler.specifications.base.ScheduledTaskExecutionLogSpecification;
import io.github._0xorigin.queryfilterbuilder.FilterContext;
import io.github._0xorigin.queryfilterbuilder.SortContext;
import io.github._0xorigin.queryfilterbuilder.base.filteroperator.Operator;
import lombok.Getter;

@Getter
public class ScheduledTaskExecutionLogSpecificationImpl implements ScheduledTaskExecutionLogSpecification {
    private final FilterContext.Template<ScheduledTaskExecutionLogEntity> filterTemplate = generateFilterTemplate();
    private final SortContext.Template<ScheduledTaskExecutionLogEntity> sortTemplate = generateSortTemplate();

    private FilterContext.Template<ScheduledTaskExecutionLogEntity> generateFilterTemplate() {
        return FilterContext.buildTemplateForType(ScheduledTaskExecutionLogEntity.class)
                .queryParam(builder ->
                        builder
                            .addFilter("startedAt", Operator.GTE, Operator.LT, Operator.GT, Operator.LTE, Operator.BETWEEN, Operator.NOT_BETWEEN)
                            .addFilter("finishedAt", Operator.GTE, Operator.LT, Operator.GT, Operator.LTE, Operator.BETWEEN, Operator.NOT_BETWEEN)
                            .addFilter("status", Operator.EQ)
                )
                .buildTemplate();
    }

    private SortContext.Template<ScheduledTaskExecutionLogEntity> generateSortTemplate() {
        return SortContext.buildTemplateForType(ScheduledTaskExecutionLogEntity.class)
                .queryParam(builder ->
                        builder
                            .addSorts("id")
                            .addSorts("startedAt")
                            .addSorts("finishedAt")
                            .addSorts("status")
                )
                .buildTemplate();
    }
}
