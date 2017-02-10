/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package thread;

/**
 * $Id CouponLinesWorker.java 2015-08-14 13:35 wangguoxing (554952580@qq.com) $
 * <p/>
 * 用于多线程统计某个券码被使用的数量
 */
public class StatWork extends Thread {

    /**
     * 第n个线程, 每个线程扫描5张分表 依次为1-5 6-10 ...
     */
    public static int n = 0;

    /**
     * 统计总数
     */
    public static int total = 0;

    /**
     * 券码
     */
    private String code;

    /**
     * 第n个线程
     */
    static synchronized void inc() {
        n++;
    }

    /**
     * 累加总数
     *
     * @param count int
     */
    static synchronized void sumTotal(int count) {
        total = total + count;
    }

    /**
     * CouponLinesWorker
     *
     * @param code String
     */
    public StatWork(String code) {
        this.code = code;
    }

    public void run() {
        try {
            // 标记当前是第几个线程
            inc();
            // 当前线程查询的表上线值， 如第一个线程查询5以下的5张表，第二个线程查询10以下的5张表
            int num = n * 5;
            for (int i = 0; i < 5; i++) {
                int hash = num - i;
                int count = 2;
                sumTotal(count);
            }
            sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            Thread[] threads = new Thread[10];
            // 初始化线程数
            StatWork.n = 0;
            // 建立10个线程
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new StatWork("abc" + i);
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

        System.out.println(StatWork.total);
    }

}