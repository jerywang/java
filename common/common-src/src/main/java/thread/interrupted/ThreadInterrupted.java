/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package thread.interrupted;

/**
 * $Id interrupted.java 2016-08-10 14:23 wangguoxing@baidu.com $
 */
public class ThreadInterrupted extends Thread {
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            long now = System.currentTimeMillis();
            while (System.currentTimeMillis() - now < 1000) {
                // 为了避免Thread.sleep()而需要捕获InterruptedException而带来的理解上的困惑,
                // 此处用这种方法空转1秒
            }
            System.out.println("Going...");
        }
        System.out.println("Someone interrupted me.");
    }

    public void cancel() {
        interrupt();
    }

    public static void main(String[] args) {
        ThreadInterrupted t = new ThreadInterrupted();
        t.start();
        long now = System.currentTimeMillis();
        while (System.currentTimeMillis() - now < 3000) {
            // 为了避免Thread.sleep()而需要捕获InterruptedException而带来的理解上的困惑,
            // 此处用这种方法空转1秒
        }
        t.cancel();
        if (t.isInterrupted()) {
            System.out.println("子线程已经中断");
        }
    }
}
