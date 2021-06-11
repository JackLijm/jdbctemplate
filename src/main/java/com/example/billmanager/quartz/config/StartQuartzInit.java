package com.example.billmanager.quartz.config;

import com.example.billmanager.quartz.job.HelloWorldJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class StartQuartzInit implements ApplicationRunner {

    @Autowired
    SchedulerFactoryBean schedulerFactoryBean;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobDetail jobDetail = JobBuilder.newJob(HelloWorldJob.class).storeDurably().build();
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds((int) TimeUnit.HOURS.toSeconds(1l)); //每一秒执行一次
               // .repeatForever(); //永久重复，一直执行下去
        SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withSchedule(scheduleBuilder)
                .build();
        scheduler.scheduleJob(jobDetail,simpleTrigger);
    }
}
