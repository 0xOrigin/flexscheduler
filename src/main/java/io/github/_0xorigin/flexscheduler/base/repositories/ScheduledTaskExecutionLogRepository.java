package io.github._0xorigin.flexscheduler.base.repositories;

import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskExecutionLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ScheduledTaskExecutionLogRepository extends JpaRepository<ScheduledTaskExecutionLogEntity, UUID>, JpaSpecificationExecutor<ScheduledTaskExecutionLogEntity> {
}
