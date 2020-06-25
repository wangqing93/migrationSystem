package com.data.migration.service.impl;

import com.data.migration.dao.destination.DestinationMapper;
import com.data.migration.service.TaskManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service("taskManagementService")
public class TaskManagementServiceImpl implements TaskManagementService {

    @Autowired
    DestinationMapper destinationMapper;

    @Override
    public void createTask(int taskId,
                           Set<String> tableNames,
                           String taskName,
                           String startTime,// 2019-11-15 18:00:00
                           int isRepeated,
                           String remarks,
                           int isAll,
                           int isDeleted) {
        // isRepeated:0  isAll: 1 isDeleted: 0
        destinationMapper.insertTask(taskId, taskName, startTime + "','yyyy-mm-dd hh24:mi:ss", isRepeated, remarks, isAll, isDeleted);
        destinationMapper.insertTableNamesForTaskId(tableNames, taskId);
    }

    @Override
    public void deleteTask(int taskId) {
        destinationMapper.deleteTask(taskId);
    }

    @Override
    public void modifyTask(int taskId, String newStartTime) {
        //newStartTime="2019-11-15 20:00:00";
        destinationMapper.modifyTask(taskId, newStartTime +"','yyyy-mm-dd hh24:mi:ss");
    }


}
