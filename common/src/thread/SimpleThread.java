/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package thread;

/**
 * Created by wangguoxing on 15-8-28.
 */
public class SimpleThread {

    public static void main(String[] args) throws Exception {
        try {
            Thread[] threads = new Thread[10];
            // 初始化线程数
            Worker.n = 0;
            // 建立10个线程
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Worker("abc");
            }
            for (int i = 0; i < threads.length; i++) {
                threads[i].start();
            }
            // 所有线程都执行完后继续
            for (int i = 0; i < threads.length; i++) {
                threads[i].join();
            }
        } catch (Exception e) {
            throw new Exception("stat coupon failed: ", e);
        }

        System.out.println(Worker.total);
    }
}
