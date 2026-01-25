package io.github._0xorigin.flexscheduler.specifications;

import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskExecutionLogEntity;
import io.github._0xorigin.flexscheduler.base.enums.ScheduledTaskExecutionStatus;
import io.github._0xorigin.flexscheduler.specifications.base.ScheduledTaskSpecification;
import io.github._0xorigin.queryfilterbuilder.FilterContext;
import io.github._0xorigin.queryfilterbuilder.SortContext;
import io.github._0xorigin.queryfilterbuilder.base.filteroperator.Operator;
import io.github._0xorigin.queryfilterbuilder.base.wrappers.FilterErrorWrapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
public class ScheduledTaskSpecificationImpl implements ScheduledTaskSpecification {
    private final FilterContext.Template<ScheduledTaskEntity> filterTemplate = generateFilterTemplate();
    private final SortContext.Template<ScheduledTaskEntity> sortTemplate = generateSortTemplate();

    private FilterContext.Template<ScheduledTaskEntity> generateFilterTemplate() {
        return FilterContext.buildTemplateForType(ScheduledTaskEntity.class)
            .queryParam(builder ->
                builder
                    .addFilter("name", Operator.EQ, Operator.CONTAINS, Operator.STARTS_WITH, Operator.ENDS_WITH)
                    .addFilter("description", Operator.EQ, Operator.CONTAINS, Operator.STARTS_WITH, Operator.ENDS_WITH)
                    .addFilter("taskType", Operator.EQ, Operator.CONTAINS, Operator.STARTS_WITH, Operator.ENDS_WITH)
                    .addFilter("cronExpression", Operator.EQ)
                    .addFilter("plannedExecutionTime", Operator.GTE, Operator.LT, Operator.GT, Operator.LTE, Operator.BETWEEN, Operator.NOT_BETWEEN)
                    .addFilter("startDateTime", Operator.GTE, Operator.LT, Operator.GT, Operator.LTE, Operator.BETWEEN, Operator.NOT_BETWEEN)
                    .addFilter("nextExecutionTime", Operator.GTE, Operator.LT, Operator.GT, Operator.LTE, Operator.BETWEEN, Operator.NOT_BETWEEN)
                    .addFilter("createdAt", Operator.GTE, Operator.LT, Operator.GT, Operator.LTE, Operator.BETWEEN, Operator.NOT_BETWEEN)
                    .addFilter("updatedAt", Operator.GTE, Operator.LT, Operator.GT, Operator.LTE, Operator.BETWEEN, Operator.NOT_BETWEEN)
                    .addFilter("lastClaimedAt", Operator.GTE, Operator.LT, Operator.GT, Operator.LTE, Operator.BETWEEN, Operator.NOT_BETWEEN)
                    .addFilter("isExecutionFinished", Operator.IS_NULL, Operator.IS_NOT_NULL, Operator.EQ)
                    .addFilter("isActive", Operator.IS_NULL, Operator.IS_NOT_NULL, Operator.EQ)
                    .addCustomFilter("tasksWithLastExecutionStatus", ScheduledTaskExecutionStatus.class, this::tasksWithLastExecutionStatus)
            )
            .buildTemplate();
    }

    private SortContext.Template<ScheduledTaskEntity> generateSortTemplate() {
        return SortContext.buildTemplateForType(ScheduledTaskEntity.class)
            .queryParam(builder ->
                builder
                    .addSorts("id")
                    .addSorts("createdAt")
                    .addSorts("isExecutionFinished")
                    .addSorts("lastClaimedAt")
            )
            .buildTemplate();
    }

    private Optional<Predicate> tasksWithLastExecutionStatus(
        Root<ScheduledTaskEntity> root,
        CriteriaQuery<?> cq,
        CriteriaBuilder cb,
        List<?> inputs,
        FilterErrorWrapper filterErrorWrapper
    ) {
        ScheduledTaskExecutionStatus status = (ScheduledTaskExecutionStatus) inputs.getFirst();

        Subquery<UUID> existsSubquery = cq.subquery(UUID.class);
        Root<ScheduledTaskExecutionLogEntity> log = existsSubquery.from(ScheduledTaskExecutionLogEntity.class);
        existsSubquery.select(log.get("id"));

        Subquery<Integer> newerExists = existsSubquery.subquery(Integer.class);
        Root<ScheduledTaskExecutionLogEntity> newer = newerExists.from(ScheduledTaskExecutionLogEntity.class);
        newerExists.select(cb.literal(1));
        newerExists.where(
            cb.equal(newer.get("task").get("id"), root.get("id")),
            cb.greaterThan(newer.get("finishedAt").as(OffsetDateTime.class), log.get("finishedAt").as(OffsetDateTime.class))
        );

        existsSubquery.where(
            cb.equal(log.get("task").get("id"), root.get("id")),
            cb.equal(log.get("status"), status),
            cb.not(cb.exists(newerExists))
        );
        return Optional.of(cb.exists(existsSubquery));
    }
}
