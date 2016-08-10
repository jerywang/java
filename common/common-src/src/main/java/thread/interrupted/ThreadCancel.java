/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package thread.interrupted;

import java.math.BigInteger;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * $Id ThreadCancel.java 2016-07-26 16:16 wangguoxing@baidu.com $
 *
 * 到目前为止一切顺利!但是，当线程等待某些事件发生而被阻塞，又会发生什么？当然，如果线程被阻塞，它便不能核查共享变量，也就不能停止。
 * 这在许多情况下会发生，例如调用Object.wait()、ServerSocket.accept()和DatagramSocket.receive()时
 */
public class ThreadCancel implements Runnable {
    private final List<BigInteger> primes = Lists.newArrayList();
    private volatile boolean cancelled;

    public void run() {
        BigInteger p = BigInteger.ONE;
            while (true) {
                if (!cancelled) {
                    p = p.nextProbablePrime();
                    synchronized(this) {
                        primes.add(p);
                    }
                } else {
                    System.out.println("Someone interrupted me.");
                    break;
                }
            }
    }

    public void cancel() {
        cancelled = true;
    }

    public synchronized List<BigInteger> get() {
        return Lists.newArrayList(primes);
    }

    public static void main(String[] args) {
        long now = System.currentTimeMillis();
        ThreadCancel threadCancel = new ThreadCancel();
        new Thread(threadCancel).start();
        while (System.currentTimeMillis() - now < 50) {
            // 为了避免Thread.sleep()而需要捕获InterruptedException而带来的理解上的困惑,
            // 此处用这种方法空转1秒
        }
        threadCancel.cancel();
        System.out.println("\n" + threadCancel.get());
    }
}