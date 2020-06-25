package com.data.migration.controller;

import com.data.migration.service.TaskManagementService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin
@RestController
public class TaskManagementController {

    @Resource(name = "taskManagementService")
    TaskManagementService  taskManagementService;

    @RequestMapping("/insertTask")
    public String testInsertTask(@RequestParam(name = "task_id") int taskId,
                                 @RequestBody String tableNames,
                                 @RequestParam(name = "taskName") String taskName,
                                 @RequestParam(name = "startTime") String startTime,// 2019-11-15 18:00:00
                                 @RequestParam(name = "isRepeated") int isRepeated,
                                 @RequestParam(name = "remarks") String remarks,
                                 @RequestParam(name = "isAll") int isAll,
                                 @RequestParam(name = "isDeleted") int isDeleted) {
        String[] tableArray = tableNames.split(",");
        taskManagementService.createTask(taskId, new HashSet<>(Arrays.asList(tableArray)), taskName, startTime, isRepeated, remarks, isAll, isDeleted);
        return "{\"status\": 200, \"message\": \"insert successful\"}";
    }

    @RequestMapping("/deleteTask")
    public String testDeleteTask(@RequestParam(name = "task_id") int taskId) {
        taskManagementService.deleteTask(taskId);
        return "{\"status\": 200, \"message\": \"delete successful\"}";
    }


    @RequestMapping("/modifyTask")
    public String testModifyTask(@RequestParam(name = "task_id") int taskId,
                                 @RequestParam(name = "start_time") String newStartTime) {
        taskManagementService.modifyTask(taskId,newStartTime);
        return "{\"status\": 200, \"message\": \"modify successful\"}";
    }
}
