/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package cache;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * $Id Memoryer.java 2016-06-28 17:58 wangguoxing (554952580@qq.com) $
 *
 * 缓存实现, 且并发场景中不会出现同一缓存被重复计算
 */
public class Memoryer<A, V> implements Computable<A, V> {

    private final ConcurrentMap<A, Future<V>> cache = new ConcurrentHashMap<A, Future<V>>();

    private final Computable<A, V> c;

    public Memoryer(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(final A arg) throws InterruptedException {
        while (true) {
            Future<V> future = cache.get(arg);
            if (future == null) {
                Callable<V> eval = new Callable<V>() {
                    @Override
                    public V call() throws Exception {
                        return c.compute(arg);
                    }
                };
                FutureTask<V> futureTask = new FutureTask<V>(eval);
                // 保证了原子操作
                future = cache.putIfAbsent(arg, futureTask);
                // 如果第一次被设置,则运行计算任务,保证了不会重复计算
                if (future == null) {
                    future = futureTask;
                    futureTask.run();
                }
            }
            try {
                return future.get();
            } catch (CancellationException ce) {
                cache.remove(arg, future);
            } catch (ExecutionException ee) {
                ee.printStackTrace();
            }
        }
    }
}
