/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package design.singleton;

import org.apache.http.annotation.ThreadSafe;

/**
 * $Id Singleton3.java 2016-07-01 14:00 wangguoxing@baidu.com $
 * <p/>
 * 双重检测同步延迟加载 - 懒汉模式
 */
@ThreadSafe
public class Singleton3 {

    /**
     * 保证先行发生关系 happens-before relationship
     */
    private static volatile Singleton3 instance = null;

    private Singleton3() {}

    public static Singleton3 getInstance() {
        if (instance == null) {
            synchronized(Singleton3.class) {
                if (instance == null) {
                    instance = new Singleton3();
                }
            }
        }
        return instance;
    }

}
