/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * $Id LockTest.java Aug 10,2016 wangguoxing (554952580@qq.com) $
 */
public class LockTest {

    private final Lock lock = new ReentrantLock();
    private String name;

    public  void setName(String name) {
        lock.lock();
        try {
            this.name = name;
        } finally {
            lock.unlock();
        }
    }

    public  String getName() {
        lock.lock();
        try {
            return this.name;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        final LockTest lockTest = new LockTest();
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    lockTest.setName(Thread.currentThread() + "-name");
                    System.out.println("\n"+lockTest.getName());
                }
            };
            service.execute(run);
        }
        service.shutdown();

        // java -Dkey=VALUE lock.LockTest 测试环境变量
        System.out.println(System.getProperty("key"));
    }
}
