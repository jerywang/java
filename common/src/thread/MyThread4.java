/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package thread;

import java.util.concurrent.Callable;

/**
 * Created by wangguoxing on 15-11-24.
 */
public class MyThread4 implements Callable<Integer> {
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
}

//public class MyThread4 implements Runnable {
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