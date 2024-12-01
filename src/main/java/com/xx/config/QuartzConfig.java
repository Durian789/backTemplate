package com.xx.config;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QuartzConfig implements ApplicationListener<ContextRefreshedEvent> {

    private final Scheduler scheduler;

    public QuartzConfig(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        /*try {
            JobDetail job = JobBuilder.newJob(Job.class)
                    .withIdentity("myJob", "group1")
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("myTrigger", "group1")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(5) // 每5秒执行一次
                            .repeatForever())
                    .build();

            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }*/
    }
}