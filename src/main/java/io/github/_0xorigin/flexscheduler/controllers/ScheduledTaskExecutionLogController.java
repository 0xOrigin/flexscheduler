package io.github._0xorigin.flexscheduler.controllers;

import io.github._0xorigin.flexscheduler.dtos.responses.ScheduledTaskExecutionLogListResponse;
import io.github._0xorigin.flexscheduler.dtos.responses.ScheduledTaskExecutionLogRetrieveResponse;
import io.github._0xorigin.flexscheduler.services.base.ScheduledTaskExecutionLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/scheduled-tasks/{taskId}/execution-logs")
@RequiredArgsConstructor
public class ScheduledTaskExecutionLogController {

    private final ScheduledTaskExecutionLogService logService;

    @GetMapping
    public ResponseEntity<List<ScheduledTaskExecutionLogListResponse>> list(
        HttpServletRequest httpServletRequest,
        @PathVariable("taskId") UUID taskId
    ){
        return ResponseEntity.ok(logService.list(httpServletRequest, taskId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduledTaskExecutionLogRetrieveResponse> retrieve(
        @PathVariable("taskId") UUID taskId,
        @PathVariable("id") UUID id
    ){
        return ResponseEntity.ok(logService.retrieve(taskId, id));
    }
}
