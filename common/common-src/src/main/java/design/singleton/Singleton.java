/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package design.singleton;

/**
 * $Id Singleton.java 2016-07-01 14:40 wangguoxing@baidu.com $
 *
 * Effective Java 2.3 使用枚举类型强化Singleton
 *
 * 优点: 自由序列化, 不会产生新的对象 保证只有一个实例, 线程安全
 */
public enum Singleton {

    INSTANCE;

    private Singleton() {}

    public void execute() {
        System.out.println("the strongest singleton from enum");
    }
}
