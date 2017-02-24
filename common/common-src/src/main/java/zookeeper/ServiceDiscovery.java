/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package zookeeper;

import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import lombok.extern.log4j.Log4j;

/**
 * ServiceDiscovery.java 12 May 2016 wangguoxing
 * <p>
 * Description: client 发现服务
 */
@Log4j
public class ServiceDiscovery {

    private ZooKeeper zk;
    private volatile List<String> serverList = new ArrayList<String>();

    private String registryAddress;

    public List<String> getServerList() {
        return serverList;
    }

    public ServiceDiscovery(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public void discover() {
        zk = ZooKeeperFactory.getServer(registryAddress, new Watcher() {
            public void process(WatchedEvent event) {
                // 如果发生了"/demo"节点下的子节点变化事件, 更新server列表, 并重新注册监听
                if (event.getType() == Event.EventType.NodeChildrenChanged
                        && Constant.ZK_SERVER_PATH.equals(event.getPath())) {
                        updateServerList();
                }
            }
        });
        updateServerList();
    }

    private void updateServerList() {
        try {
            List<String> newServerList = new ArrayList<String>();
            // 获取并监听groupNode的子节点变化
            // watch参数为true, 表示监听子节点变化事件.
            // 每次都需要重新注册监听, 因为一次注册, 只能监听一次事件, 如果还想继续保持监听, 必须重新注册
            List<String> subList = zk.getChildren(Constant.ZK_SERVER_PATH, true);
            for (String subNode : subList) {
                // 获取每个子节点下关联的server地址
                byte[] data = zk.getData(Constant.ZK_SERVER_PATH + "/" + subNode, false, new Stat());
                newServerList.add(new String(data, "utf-8"));
            }
            this.serverList = newServerList;
            System.out.println("server list updated: " + serverList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ServiceDiscovery serviceDiscovery = new ServiceDiscovery("127.0.0.1:2181");
        serviceDiscovery.discover();
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
