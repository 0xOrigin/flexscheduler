package io.github._0xorigin.flexscheduler.controllers;

import io.github._0xorigin.flexscheduler.base.dtos.CreateScheduledTaskRequest;
import io.github._0xorigin.flexscheduler.base.entities.ScheduledTaskEntity;
import io.github._0xorigin.flexscheduler.base.repositories.ScheduledTaskRepository;
import io.github._0xorigin.flexscheduler.services.ScheduledTaskManagementService;
import io.github._0xorigin.flexscheduler.services.TaskSchedulerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scheduled-tasks")
@RequiredArgsConstructor
public class ScheduledTaskController {

    private final TaskSchedulerService taskSchedulerService;
    private final ScheduledTaskRepository taskRepository;
    private final ScheduledTaskManagementService managementService;

    @GetMapping
    public ResponseEntity<?> list(){
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> createTask(@Valid @RequestBody CreateScheduledTaskRequest request) {
        ScheduledTaskEntity taskEntity = managementService.createAndSave(request);
        taskSchedulerService.scheduleTaskIfExecuteToday(taskEntity);
        return ResponseEntity.ok(taskEntity);
    }
}

