/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package thread;

/**
 * Created by wangguoxing on 15-11-16.
 */
public class MyThread2 implements Runnable {
    private int tickets = 10;

    public void run() {
        while (true) {
            if (tickets > 0) {
                System.out.println(Thread.currentThread().getName() +
                        " is saling ticket " + tickets--);
            }
        }
    }
}
