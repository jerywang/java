/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package thread;

/**
 * Created by wangguoxing on 15-11-16.
 */
public class TestMyThread2 {
    public static void main(String[] args) {
        MyThread2 mt = new MyThread2();
        new Thread(mt).start();//同一个mt，但是在Thread中就不可以，如果用同一
        new Thread(mt).start();//个实例化对象mt，就会出现异常
        new Thread(mt).start();
    }
}
