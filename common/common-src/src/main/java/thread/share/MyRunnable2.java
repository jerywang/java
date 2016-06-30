/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package thread.share;

/**
 * Created by wangguoxing on 15-11-26.
 */
public class MyRunnable2 implements Runnable{
    private ShareData share2 = null;
    public MyRunnable2(ShareData share2) {
        this.share2 = share2;

    }
    public void run() {
        for(int i = 0;i<10;i++){
            share2.subx();
        }
        share2.getX();
    }
}