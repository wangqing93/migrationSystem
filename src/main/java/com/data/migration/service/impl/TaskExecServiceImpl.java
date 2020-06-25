package com.data.migration.service.impl;

import com.data.migration.config.CronTaskRegistrar;
import com.data.migration.dao.destination.DestinationMapper;
import com.data.migration.service.TaskExecService;
import com.data.migration.service.TaskManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

@Service("taskExecService")
public class TaskExecServiceImpl implements TaskExecService {

    @Autowired
    DestinationMapper destinationMapper;

    @Autowired
    CronTaskRegistrar cronTaskRegistrar;

    @Override
    public void execAllTasks() {
        HashSet<Map<String, Object>> tasks = destinationMapper.getTasks();
        for(Map<String, Object> task : tasks) {
            BigDecimal bigDecimal= (BigDecimal)task.get("TASK_ID");
            Integer task_id=Integer.parseInt(bigDecimal.toString());
            System.out.println(task_id);
            Date date = (Date)task.get("START_TASK_TIME");
            Timestamp ts = new Timestamp(date.getTime());
            Integer isAll = Integer.parseInt(task.get("ISALL").toString());
            Integer is_repeated = Integer.parseInt(task.get("IS_REPEATED").toString());
            //可以精确到秒  2017-4-16 12:43:37
            SchedulingRunnable test = new SchedulingRunnable("demoTask", "taskWithParams", task_id, isAll, ts);
            String execCronExp = getSpecialTime(date, is_repeated);
            System.out.println(execCronExp);
            cronTaskRegistrar.addCronTask(test, execCronExp);
            DateFormat df2 = DateFormat.getDateTimeInstance();
            System.out.println(df2.format(date));

        }

    }

    public String getSpecialTime(Date date, Integer is_repeated){
        String str="";
        String[] strNow3 = new SimpleDateFormat("HH:mm:ss").format(date).toString().split(":");

        int hour = Integer.parseInt(strNow3[0]);			//获取时（24小时制）
        int minute = Integer.parseInt(strNow3[1]);			//获取分
        int second = Integer.parseInt(strNow3[2]);
        if(is_repeated == 1){
            return second + " " + minute + " " + hour + " * * ?";
        }
        String[] strNow2 = new SimpleDateFormat("YYYY-MM-DD").format(date).toString().split("-");
        int year = Integer.parseInt(strNow2[0]);
        int month = Integer.parseInt(strNow2[1]);
        int day = Integer.parseInt(strNow2[2]);
        return second + " " + minute + " " + hour + " " + day + " " + month + " ? " + year;
    }
}
