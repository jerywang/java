/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.netty.rpc.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import lombok.extern.log4j.Log4j;

/**
 * ZooKeeperFactory.java 15 May 2016 wangguoxing
 * <p>
 * Description:
 */
@Log4j
public class ZooKeeperFactory {

    private ZooKeeperFactory() {
    }

    public static ZooKeeper getServer(String registryAddress, final CountDownLatch latch) {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            });
            latch.await();
        } catch (Exception e) {
            log.error("connect zookeeper failed: ", e);
        }
        return zk;
    }
}
