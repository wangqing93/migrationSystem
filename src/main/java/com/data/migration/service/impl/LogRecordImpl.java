package com.data.migration.service.impl;

import com.data.migration.dao.destination.DestinationMapper;
import com.data.migration.service.LogRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service("LogRecordService")
public class LogRecordImpl implements LogRecordService {

    @Autowired
    DestinationMapper destinationMapper;

    @Override
    public void insertLog(List<Map<String, Object>> values) {
        List<String> resultList = new LinkedList<>();
        String tableName = "";
        String columnList = "";
        String itemValue = "";
        String startTime = "";
        String endTime = "";
        int isSucceed = 1;
        String errorLog = "";
        int taskId = destinationMapper.getTaskId() + 1;
        for(Map<String, Object> map : values){
            columnList = "";
            itemValue = "";
            for (String s : map.keySet()) {
                if (s.equals("TABLE_NAME")){
                    columnList += (s + ", ");
                    tableName = map.get(s).toString();
                    itemValue += ("'" + tableName + "',");
                }
                if (s.equals("START_TIME")){
                    columnList += (s + ", ");
                    startTime = map.get(s).toString();
                    itemValue += ("to_date(\'" + startTime + "\', \'YYYY-MM-DD HH24:MI:SS\'), ");
                }
                if (s.equals("END_TIME")){
                    columnList += (s + ", ");
                    endTime = map.get(s).toString();
                    itemValue += ("to_date(\'" + endTime + "\', \'YYYY-MM-DD HH24:MI:SS\'), ");
                }
                if (s.equals("IS_SUCCEEDED")){
                    columnList += (s + ", ");
                    isSucceed = Integer.parseInt(map.get(s).toString());
                    itemValue += (isSucceed + ", ");
                }
                if (s.equals("ERROR_LOG")){
                    columnList += (s + ", ");
                    errorLog = map.get(s).toString();
                    itemValue += ("'" + errorLog + "', ");
                }
            }
            columnList = columnList + "TASK_ID";
            itemValue = itemValue + taskId;
//            columnList = columnList.substring(0, columnList.length() - 2);
            columnList = "(" + columnList + ")";
//            itemValue = itemValue.substring(0, itemValue.length() - 2);
            itemValue = "(" + itemValue + ")";
            resultList.add("into TASK_LOG" + columnList + "values " + itemValue);
        }
        destinationMapper.insertNotBlobValue(resultList);
    }
}
