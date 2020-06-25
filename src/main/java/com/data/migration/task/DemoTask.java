package com.data.migration.task;

import com.data.migration.dao.destination.DestinationMapper;
import com.data.migration.service.DataBaseService;
import com.data.migration.service.LogRecordService;
import com.data.migration.service.MigrationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: simple-demo
 * @description:
 * @author: CaoTing
 * @date: 2019/5/23
 **/
@Component("demoTask")
public class DemoTask {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    @Autowired
    DestinationMapper destinationMapper;

    @Autowired
    MigrationService migrationService;

    @Autowired
    DataBaseService dataBaseService;

    @Resource(name = "LogRecordService")
    LogRecordService LogRecordService;

    public void taskWithParams(Integer taskId, Integer isAll, Timestamp ts) throws NoSuchAlgorithmException, SQLException, JsonProcessingException {
//        try {
            System.out.println("start 这是有参任务,任务Id" + taskId);
            HashSet<Map<String, Object>> tables = destinationMapper.getTablesOneTask(taskId);
            for(Map<String, Object> table : tables) {
                String lastUpdateTime = dateFormat.format(dataBaseService.getStartTime((String) table.get("TABLE_NAME")));
                List<Map<String, Object>> listMaps = new ArrayList<Map<String, Object>>();
                Map<String, Object> map1 = new HashMap<String, Object>();
                HashSet<String> tableNames = new HashSet<>();
                map1.put("TABLE_NAME", (String) table.get("TABLE_NAME"));
                map1.put("START_TIME", dateFormat.format(new Date()));
                tableNames.add((String) table.get("TABLE_NAME"));
                String message = isAll == 1 ? migrationService.allMigration(tableNames) : migrationService.updateMigration(tableNames, lastUpdateTime);
                map1.put("END_TIME", dateFormat.format(new Date()));
                if(message.equals("OK")){
                    map1.put("IS_SUCCEEDED", 1);
                    map1.put("ERROR_LOG", message);
                } else {
                    map1.put("IS_SUCCEEDED", 0);
                    map1.put("ERROR_LOG", message);
                }
                listMaps.add(map1);
                LogRecordService.insertLog(listMaps);
            }
            // one log data
            System.out.println("end 这是有参任务,任务Id" + taskId);
//
//        }
//    } catch (InvocationTargetException e){
//            System.out.println("+++++++++++++++++++++++++++");
    }

    public void taskNoParams() {
        System.out.println("执行无参示例任务");
    }
}
