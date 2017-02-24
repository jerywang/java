/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package zookeeper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import lombok.extern.log4j.Log4j;

/**
 * ServiceRegistry.java 12 May 2016 wangguoxing
 * <p>
 * Description: register host port, services
 */
@Log4j
public class ServiceRegistry {

    private ZooKeeper zk;
    private volatile List<String> serverList = new ArrayList<String>();

    public ServiceRegistry(String registryAddress) {
        this.zk = ZooKeeperFactory.getServer(registryAddress);
    }

    public void register(String data) {
        if (data != null) {
            if (zk != null) {
                createNode(zk, data);
            }
        }
    }

    public void createNode(ZooKeeper zk, String data) {
        try {
            // 子节点的类型设置为EPHEMERAL_SEQUENTIAL, 表明这是一个临时节点, 且在子节点的名称后面加上一串数字后缀
            String path = zk.create(Constant.ZK_SERVER_DATA_PATH, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);
            log.info("create zookeeper node: " + path + data);
        } catch (Exception e) {
            log.error("create zookeeper node failed:", e);
        }
    }

    public static void main(String[] args) {
        ServiceRegistry serviceRegistry = new ServiceRegistry("127.0.0.1:2181");
        serviceRegistry.register("192.168.1.10:2000");
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
