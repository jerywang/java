/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * $Id AtomicTest.java Oct 25,2016 wangguoxing (554952580@qq.com) $
 */
public class AtomicTest {
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public int increase() {
        return atomicInteger.incrementAndGet();
    }

    public int getAtomicInteger() {
        return atomicInteger.get();
    }

    private int integer = 0;

    public int increaseInt() {
        return integer++;
    }

    public int getInteger() {
        return integer;
    }


    public static void main(String[] args) {
        final AtomicTest atomicTest = new AtomicTest();
        ExecutorService es = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10000; i++) {
            es.execute(new Runnable() {
                @Override
                public void run() {
                    atomicTest.increase();
                    atomicTest.increaseInt();
                }
            });
        }
        try {
            Thread.sleep(3000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("atomicTest.getAtomicInteger()" + atomicTest.getAtomicInteger());
        System.out.println("atomicTest.getInteger()" + atomicTest.getInteger());
    }
}
