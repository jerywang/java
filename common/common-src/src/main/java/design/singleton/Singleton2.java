/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package design.singleton;

import org.apache.http.annotation.ThreadSafe;

/**
 * $Id Singleton1.java 2016-07-01 13:49 wangguoxing (554952580@qq.com) $
 * <p/>
 * 同步延迟加载 - 懒汉模式
 */
@ThreadSafe
public class Singleton2 {

    private static Singleton2 instance = null;

    private Singleton2() {}

    public static synchronized Singleton2 getInstance() {
        if (instance == null) {
            instance = new Singleton2();
        }
        return instance;
    }

    public static String getStr() {
        return "test Singleton";
    }
}
