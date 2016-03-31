/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package thread.demo;

/**
 * Created by wangguoxing on 15-11-24.
 */
public class TaskThread implements Runnable {
    public void run() {
        try {
            Thread.sleep(5000L);
            System.out.println(Thread.currentThread().getName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
