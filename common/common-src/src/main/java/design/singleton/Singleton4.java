/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package design.singleton;

/**
 * $Id Singleton4.java 2016-07-01 14:02 wangguoxing (554952580@qq.com) $
 *
 * 静态内部类机制
 */
public class Singleton4 {

    private static class LazyHolder {
        private static final Singleton4 INSTANCE = new Singleton4();
    }

    private Singleton4() {}

    public static Singleton4 getInstance() {
        return LazyHolder.INSTANCE;
    }

}
