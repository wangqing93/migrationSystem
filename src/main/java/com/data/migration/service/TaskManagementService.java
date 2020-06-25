package com.data.migration.service;

import com.data.migration.dao.destination.DestinationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public interface TaskManagementService {

    void createTask(int taskId,
                    Set<String> tableNames,
                    String taskName,
                    String startTime,// 2019-11-15 18:00:00
                    int isRepeated,
                    String remarks,
                    int isAll,
                    int isDeleted);

    void deleteTask(int taskId);

    void modifyTask(int taskId, String newStartTime);
}
