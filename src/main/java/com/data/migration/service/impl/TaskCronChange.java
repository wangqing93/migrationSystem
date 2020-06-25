package com.data.migration.service.impl;

import org.slf4j.LoggerFactory;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
//@EnableScheduling
public class TaskCronChange implements SchedulingConfigurer {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(TaskCronChange.class);
    public static String cron;

    public TaskCronChange() {
        cron = "0/5 * * * * *";
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//        System.out.println(cron);
        Runnable task = new Runnable() {
            @Override
            public void run() {
                LOG.info("TaskCronChange task is running ... " + new Date());
            }
        };
        Trigger trigger = new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                CronTrigger trigger = new CronTrigger(cron);
                Date nextExec = trigger.nextExecutionTime(triggerContext);
                return nextExec;
            }
        };
        taskRegistrar.addTriggerTask(task, trigger);
    }
}
