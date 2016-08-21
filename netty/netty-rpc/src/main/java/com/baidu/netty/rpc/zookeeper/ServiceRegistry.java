/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.netty.rpc.zookeeper;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.log4j.Log4j;

/**
 * ServiceRegistry.java 12 May 2016 wangguoxing
 * <p>
 * Description: register host port, services
 */
@Log4j
public class ServiceRegistry {

    private CountDownLatch latch = new CountDownLatch(1);

    private String registryAddress;

    /**
     * registered service map
     */
    private Map<String, Object> serviceMap = new HashMap<String, Object>();

    public ServiceRegistry(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public Map<String, Object> getServices() {
        return serviceMap;
    }

    public void register(String data, List<Object> registerServices) {
        if (data != null) {
            ZooKeeper zk = ZooKeeperFactory.getServer(registryAddress, latch);
            if (zk != null) {
                ZooKeeperClient zooKeeperClient = new ZooKeeperClient();
                zooKeeperClient.createNode(zk, data);
            }
        }
        for (Object service : registerServices) {
            if (!serviceMap.containsKey(service.getClass().getName())) {
                serviceMap.put(service.getClass().getName(), service);
            }
        }
    }

    public void unRegister() {
        if (serviceMap != null) {
            serviceMap.clear();
        }
    }

}
