/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package thread.demo;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * $Id ThreadPoolTaskExecutorTest.java 2016-05-27 11:00 wangguoxing (554952580@qq.com) $
 *
 * Description: 多线程闭锁的使用 @see java并发编程实战 5.5.1 page 93
 */
public class ThreadPoolTaskExecutorTest implements Callable<String> {

    private CountDownLatch latch;
    private String workerName;
    private long workTime;

    public ThreadPoolTaskExecutorTest(String workerName, long workTime, CountDownLatch latch) {
        this.workerName = workerName;
        this.workTime = workTime;
        this.latch = latch;
    }

    public String call() {
        try {
            Thread.sleep(workTime); //工人开始干活
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            latch.countDown(); //工人完成工作，计数器减一
        }
        return "工作者: " + workerName + " 工作了: " + workTime + " s";
    }

    public static void main(String[] args) throws Exception {
        ArrayList<Future<String>> results = new ArrayList<Future<String>>();
        int workerNum = 10; //五个线程的协作10个任务
        CountDownLatch latch = new CountDownLatch(workerNum);
        ThreadPoolTaskExecutor taskExecutor = ThreadPoolTaskExecutorTest.getPoolTaskExecutor();
        for (int k = 0; k < workerNum; k++) {
            results.add(taskExecutor.submit(new ThreadPoolTaskExecutorTest("name" + k, 3000L, latch)));
        }
        if (latch.await(10000, TimeUnit.MILLISECONDS)) {
            System.out.println("all the threads end!");
        } else {
            System.out.println("time out!");
        }
        taskExecutor.shutdown();
        for (Future future : results) {
//            if (future.isDone()) {
                System.out.println(future.get());
            //future.get(3L, TimeUnit.SECONDS);//timeOut
//            }
        }
    }

    public static ThreadPoolTaskExecutor getPoolTaskExecutor() {
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        //线程池所使用的缓冲队列
        poolTaskExecutor.setQueueCapacity(200);
        //线程池维护线程的最少数量
        poolTaskExecutor.setCorePoolSize(5);
        //线程池维护线程的最大数量
        poolTaskExecutor.setMaxPoolSize(1000);
        //线程池维护线程所允许的空闲时间
        poolTaskExecutor.setKeepAliveSeconds(30000);
        poolTaskExecutor.initialize();

        return poolTaskExecutor;
    }
}