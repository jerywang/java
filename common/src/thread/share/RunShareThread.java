/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package thread.share;

/**
 * Created by wangguoxing on 15-11-26.
 */
public class RunShareThread {
    public static void main(String[] args) {
        ShareThread thread = new ShareThread();
        Thread a = new Thread(thread,"A");
        Thread b = new Thread(thread,"B");
        Thread c = new Thread(thread,"C");
        a.start();
        b.start();
        c.start();
    }
}
