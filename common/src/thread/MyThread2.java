/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package thread;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangguoxing on 15-11-24.
 *
 */
public class MyThread2 implements Callable<Integer> {
    public Integer call() throws Exception {
        System.out.println("子线程在进行计算");
        System.out.println(Thread.currentThread().getName());
        Thread.sleep(3000);
        int sum = 0;
        for (int i = 1; i <= 10; i++) {
            sum += i;
        }
        return sum;
    }

    public static void main(String[] args) {
        try {
            // 多线程处理订单请求
            CountDownLatch latch = new CountDownLatch(3);
            ExecutorService executor = Executors.newFixedThreadPool(3);
            Future future;
            for (int i = 0; i < 3; i++) {
                future = executor.submit(new MyThread2());
                System.out.println(future.get());
            }
            executor.shutdown();

            boolean result = latch.await(4, TimeUnit.SECONDS);

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

//public class MyThread2 implements Runnable {
//
//    private int value = 0;
//
//    synchronized void setValue(int value) {
//        this.value += value;
//    }
//
//    public void getValue() {
//        System.out.println(value);
//    }
//
//    public void run() {
//        try {
//            Thread.sleep(5000);
//            for (int i = 0; i < 5; i++) {
//                setValue(i);
//            }
//            getValue();
//        } catch (Exception e){}
//    }
//
//}