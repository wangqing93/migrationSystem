package com.data.migration.controller;

import com.data.migration.service.TaskExecService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.net.ConnectException;

@CrossOrigin
@RestController
public class TaskExecController {
    @Resource(name = "taskExecService")
    TaskExecService taskExecService;

    @RequestMapping("/test_exec_task")
    public String testExecTask() throws ConnectException {
        taskExecService.execAllTasks();
        return "{\"status\": 200, \"message\": \"all tasks exec successful\"}";
    }
}
