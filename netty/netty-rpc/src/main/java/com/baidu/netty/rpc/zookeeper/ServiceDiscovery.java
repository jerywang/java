/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.netty.rpc.zookeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;

import io.netty.util.internal.ThreadLocalRandom;
import lombok.extern.log4j.Log4j;

/**
 * ServiceDiscovery.java 12 May 2016 wangguoxing
 * <p>
 * Description:
 */
@Log4j
public class ServiceDiscovery {

    private volatile List<String> dataList = new ArrayList<String>();

    private String registryAddress;

    public ServiceDiscovery(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public String discover() {
        ZooKeeper zk = ZooKeeperFactory.getServer(registryAddress, new CountDownLatch(1));
        if (zk != null) {
            ZooKeeperClient zooKeeperClient = new ZooKeeperClient();
            dataList = zooKeeperClient.watchNode(zk);
        }
        String data = null;
        int size = dataList.size();
        if (size > 0) {
            if (size == 1) {
                data = dataList.get(0);
                log.info("using only data: " + data);
            } else {
                data = dataList.get(ThreadLocalRandom.current().nextInt(size));
                log.info("using random data: " + data);
            }
        }
        return data;
    }
}
