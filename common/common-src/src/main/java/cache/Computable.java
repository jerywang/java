/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package cache;

/**
 * $Id Computable.java 2016-06-28 17:56 wangguoxing@baidu.com $
 */
public interface Computable<A, V> {
    public V compute(A arg) throws InterruptedException;
}
