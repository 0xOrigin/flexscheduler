package io.github._0xorigin.flexscheduler.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scheduled-tasks/{taskId}/execution-logs")
@RequiredArgsConstructor
public class ScheduledTaskExecutionLogController {

}
