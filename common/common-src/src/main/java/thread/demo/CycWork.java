/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package thread.demo;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * $Id CycWork.java 2016-06-28 12:46 wangguoxing@baidu.com $
 * <p/>
 * Description: 多线程栅栏的使用 @see java并发编程实战 5.5.4 page 97
 * 接着上面的例子，还是这三个工人，不过这一次，这三个工人自由了，老板不用检查他们任务了，他们三个合作建桥，有三个桩，每人打一个
 * 同时打完之后才能一起搭桥（搭桥需要三人一起合作）。也就是说三个人都打完桩之后才能继续工作
 */
public class CycWork implements Runnable {

    private CyclicBarrier cyclicBarrier;
    private String name;

    public CycWork(CyclicBarrier cyclicBarrier, String name) {
        this.name = name;
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        System.out.println(name + "正在打桩，毕竟不轻松。。。。。");

        try {
            Thread.sleep(5000);
            System.out.println(name + "不容易，终于把桩打完了。。。。");
            cyclicBarrier.await();

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(name + "：其他逗b把桩都打完了，又得忙活了。。。");

    }

    public static void main(String[] args) {
        ExecutorService executorPool = Executors.newFixedThreadPool(3);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);

        CycWork work1 = new CycWork(cyclicBarrier, "张三");
        CycWork work2 = new CycWork(cyclicBarrier, "李四");
        CycWork work3 = new CycWork(cyclicBarrier, "王五");

        executorPool.execute(work1);
        executorPool.execute(work2);
        executorPool.execute(work3);

        executorPool.shutdown();
    }
}
