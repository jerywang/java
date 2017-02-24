/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * $Id ShareBean.java Feb 23,2017 wangguoxing@baidu.com $
 */
public class ShareBean2 {
    private final Lock lock = new ReentrantLock();
    private String name;

    public void setName(String name) {
        if(lock.tryLock()) {
            try {
                Thread.sleep(2000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                this.name = name;
            } finally {
                lock.unlock();
            }
        } else {
            this.name = "oh have not lock";
        }
    }

    public String getName() {
        if(lock.tryLock()) {
            try {
                Thread.sleep(2000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                return this.name;
            } finally {
                lock.unlock();
            }
        } else {
            return name;
        }
    }
}
