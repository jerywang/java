/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package cache;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * $Id SimpleCache.java 2016-06-29 11:31 wangguoxing (554952580@qq.com) $
 */
public final class SimpleCache<K, V> {

    private final Lock lock = new ReentrantLock();
    private final int maxCapacity;
    private final Map<K, V> eden;
    private final Map<K, V> perm;

    public SimpleCache(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.eden = new ConcurrentHashMap<K, V>(maxCapacity);
        this.perm = new WeakHashMap<K, V>(maxCapacity);
    }

    public V get(K k) {
        V v = this.eden.get(k);
        if (v == null) {
            lock.lock();
            try {
                v = this.perm.get(k);
            } finally {
                lock.unlock();
            }
            if (v != null) {
                this.eden.put(k, v);
            }
        }
        return v;
    }

    public void put(K k, V v) {
        if (this.eden.size() >= maxCapacity) {
            lock.lock();
            try {
                this.perm.putAll(this.eden);
            } finally {
                lock.unlock();
            }
            this.eden.clear();
        }
        this.eden.put(k, v);
    }

    public static void main(String[] args) {
        SimpleCache cache = new SimpleCache(1024);
        cache.put("key", "value");
        System.out.println(cache.get("key"));
    }
}