/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package design.singleton;

import org.apache.http.annotation.ThreadSafe;

/**
 * $Id Singleton1.java 2016-07-01 13:49 wangguoxing@baidu.com $
 * <p/>
 * 非延迟加载机制 - 饿汉模式
 */
@ThreadSafe
public class Singleton1 {

    private static final Singleton1 instance = new Singleton1();

    private Singleton1() {}

    public static Singleton1 getInstance() {
        return instance;
    }

    public static String getStr() {
        return "test Singleton";
    }

}
