/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package thread.share;

/**
 * Created by wangguoxing on 15-11-26.
 */
public class ShareData {
    private int x = 0;

    public synchronized void addx(){
        x++;
        //System.out.println(Thread.currentThread().getName()+" x++ : "+x);
    }

    public synchronized void subx(){
        x--;
        //System.out.println("x-- : "+x);
    }

    public void getX() {
        System.out.println(x);
    }
}
