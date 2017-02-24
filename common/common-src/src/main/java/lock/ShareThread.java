/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package lock;

/**
 * $Id ShareThread.java Feb 23,2017 wangguoxing@baidu.com $
 */
public class ShareThread implements Runnable {
    private ShareBean2 shareBean;

    public ShareThread() {
        this.shareBean = new ShareBean2();
    }

    @Override
    public void run() {
        System.out.print("获取lock前");
        shareBean.setName(Thread.currentThread() + "-name");
        System.out.println(shareBean.getName());
        System.out.print("获取lock后");
    }
}
