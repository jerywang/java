/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package thread;

/**
 * Created by wangguoxing on 15-11-25.
 * 多线程重排序测试
 */
public class ReSortTest extends Thread {
    public void run() {
        for (int i = 0; i < 10000; i++) {
            System.out.print("child");
        }
    }

    public static void main(String[] args) throws Exception {
        ReSortTest reSortTest = new ReSortTest();
        reSortTest.start();
        for (int i = 0; i < 10000; i++) {
            System.out.print("main");
        }
    }
}
