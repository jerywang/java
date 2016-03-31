/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangguoxing on 15-11-16.
 *
 * 资源不共享
 */
public class TestMyThread3 {
    public static void main(String[] args) {
        try {
            // 多线程处理订单请求
            CountDownLatch latch = new CountDownLatch(3);
            ExecutorService executor = Executors.newFixedThreadPool(3);
            for (int i = 0; i < 3; i++) {
                executor.submit(new MyThread3(latch));
            }
            executor.shutdown();

            boolean result = latch.await(1000, TimeUnit.SECONDS);
            
            if (result) {
                System.out.println("all the threads end!");
            } else {
                System.out.println("time out!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
