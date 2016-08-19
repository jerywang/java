/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package cache;

/**
 * $Id Computable.java 2016-06-28 17:56 wangguoxing@baidu.com $
 */
public interface Computable<A, V> {
    /**
     * 计算任务
     * @param arg 计算参数
     * @return 计算结果
     * @throws InterruptedException
     */
    public V compute(A arg) throws InterruptedException;
}
