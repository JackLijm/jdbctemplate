package com.example.billmanager.common.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadTest {
    private static Logger logger = LoggerFactory.getLogger(ThreadTest.class);;

    public ThreadTest() {

    }

    public static void start(){
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(10);
        threadPool.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                logger.info("hello world");
            }
        },10,100, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        start();
        start();
    }
}
