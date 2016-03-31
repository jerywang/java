/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package thread.demo;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by wangguoxing on 15-11-24.
 */
public class Test {

    public ThreadPoolTaskExecutor getPoolTaskExecutor() {
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

    public static void main(String[] args) throws Exception {
        Test test = new Test();
        ThreadPoolTaskExecutor taskExecutor = test.getPoolTaskExecutor();
        for(int k = 0; k < 5; k++) {
            taskExecutor.execute(new TaskThread());
        }
        Thread.sleep(6000);
        taskExecutor.shutdown();
    }

}
