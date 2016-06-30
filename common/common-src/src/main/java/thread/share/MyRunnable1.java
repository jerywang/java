/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package thread.share;

/**
 * Created by wangguoxing on 15-11-26.
 */
public class MyRunnable1 implements Runnable{
    private ShareData share1 = null;
    public MyRunnable1(ShareData share1) {
        this.share1 = share1;

    }
    public void run() {
        for(int i = 0;i<10;i++){
            share1.addx();
        }
        share1.getX();
    }
}
