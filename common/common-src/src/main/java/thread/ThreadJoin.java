/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package thread;

/**
 * $Id ThreadJoin.java Sep 28,2016 wangguoxing (554952580@qq.com) $
 */
public class ThreadJoin {
    public static void main(String[] args) throws Exception {
        ThreadChild thread1 = new ThreadChild();
        ThreadChild thread2 = new ThreadChild();
        System.out.println(System.currentTimeMillis());
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        // thread.join()会阻塞当前线程, 只有子线程全部执行完成主线程才能继续
        System.out.println(System.currentTimeMillis());
        System.out.println("all thread over!");
    }

    public static class ThreadChild extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程" + Thread.currentThread().getName());
        }
    }

}
