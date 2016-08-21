/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.netty.rpc.zookeeper;

import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;

/**
 * ZooKeeperClient.java 15 May 2016 wangguoxing
 * <p>
 * Description:
 */
@Log4j
public class ZooKeeperClient {

    public List<String> watchNode(final ZooKeeper zk) {
        List<String> dataList = new ArrayList<String>();
        try {
            List<String> nodeList = zk.getChildren(Constant.ZK_REGISTRY_PATH, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getType() == Event.EventType.NodeChildrenChanged) {
                        watchNode(zk);
                    }
                }
            });
            for (String node : nodeList) {
                byte[] bytes = zk.getData(Constant.ZK_REGISTRY_PATH + "/" + node, false, null);
                dataList.add(new String(bytes));
            }
            log.info("discovery node data: " + dataList);
            return dataList;
        } catch (Exception e) {
            log.error("discovery node data failed: ", e);
        }
        return dataList;
    }

    public void createNode(ZooKeeper zk, String data) {
        try {
            byte[] bytes = data.getBytes();
            String path = zk.create(Constant.ZK_DATA_PATH, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);
            log.info("create zookeeper node: " + path + data);
        } catch (Exception e) {
            log.error("create zookeeper node failed:", e);
        }
    }
}
