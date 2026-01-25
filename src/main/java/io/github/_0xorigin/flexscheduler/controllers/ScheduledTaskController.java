package io.github._0xorigin.flexscheduler.controllers;

import io.github._0xorigin.flexscheduler.base.dtos.CreateScheduledTaskRequest;
import io.github._0xorigin.flexscheduler.dtos.ScheduledTaskListResponse;
import io.github._0xorigin.flexscheduler.dtos.ScheduledTaskRetrieveResponse;
import io.github._0xorigin.flexscheduler.services.base.TaskSchedulerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/scheduled-tasks")
@RequiredArgsConstructor
public class ScheduledTaskController {

    private final TaskSchedulerService taskSchedulerService;

    @GetMapping
    public ResponseEntity<List<ScheduledTaskListResponse>> list(HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(taskSchedulerService.list(httpServletRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduledTaskRetrieveResponse> retrieve(@PathVariable("id") UUID id){
        return ResponseEntity.ok(taskSchedulerService.retrieve(id));
    }

    @PostMapping
    public ResponseEntity<ScheduledTaskRetrieveResponse> create(@Valid @RequestBody CreateScheduledTaskRequest request) {
        return ResponseEntity.ok(taskSchedulerService.createTask(request));
    }

    @GetMapping("/{id}/schedule")
    public ResponseEntity<?> schedule(@PathVariable("id") UUID id) {
        taskSchedulerService.scheduleTask(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/schedule-if-execute-today")
    public ResponseEntity<?> scheduleIfExecuteToday(@PathVariable("id") UUID id) {
        taskSchedulerService.scheduleTaskIfExecuteToday(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create-and-schedule")
    public ResponseEntity<ScheduledTaskRetrieveResponse> createAndSchedule(@Valid @RequestBody CreateScheduledTaskRequest request) {
        return ResponseEntity.ok(taskSchedulerService.createTaskAndSchedule(request));
    }

    @GetMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable("id") UUID id) {
        taskSchedulerService.cancelTask(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        taskSchedulerService.cancelTask(id);
        taskSchedulerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /*
      - list with filtering, for json fields- last execution failed tasks
      - update endpoint
     */
}
