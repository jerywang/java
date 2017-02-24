/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package lock;

/**
 * $Id LockTest.java Aug 10,2016 wangguoxing (554952580@qq.com) $
 */
public class LockTest {

    public static void main(String[] args) {
        ShareThread shareThread = new ShareThread();
        Thread thread1 = new Thread(shareThread);
        Thread thread2 = new Thread(shareThread);
        thread1.start();
        thread2.start();

    }
}
