/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package thread;

import java.util.concurrent.CountDownLatch;

/**
 * Created by wangguoxing on 15-11-16.
 */
public class MyThread3 implements Runnable {

    private int tickets = 10;

    private CountDownLatch latch;

    public MyThread3(CountDownLatch latch) {
        super();
        this.latch = latch;
    }

    public void run() {
        try {
            Thread.sleep(5000);
            while (true) {
                if (tickets > 0) {
                    System.out.println(Thread.currentThread().getName() + " is saling ticket " + tickets--);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }
    }

}
