/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package thread.share;

/**
 * Created by wangguoxing on 15-11-26.
 */
public class ShareThread extends Thread {
    private int count=5;
    public void run() {
        super.run();
        count--;
        System.out.println("由" + this.currentThread().getName()+ "计算,count="+count);
    }
}
