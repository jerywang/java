/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package zookeeper;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
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

    public static ZooKeeper getServer(String registryAddress) {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        log.info("connect zookeeper success!");
                    }
                }
            });
            createParent(zk);
        } catch (Exception e) {
            log.error("connect zookeeper failed: ", e);
        }
        return zk;
    }

    public static ZooKeeper getServer(String registryAddress, Watcher watcher) {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT, watcher);
            createParent(zk);
            log.info("connect zookeeper success!");
        } catch (Exception e) {
            log.error("connect zookeeper failed: ", e);
        }
        return zk;
    }

    private static void createParent(ZooKeeper zk) {
        try {
            List<String> children = zk.getChildren(Constant.ZK_SERVER_PATH, false);
//            if (CollectionUtils.isEmpty(children)) {
//                zk.create(Constant.ZK_SERVER_PATH, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
//                        CreateMode.PERSISTENT);
//            }
        } catch (Exception e) {
            try {
                zk.create(Constant.ZK_SERVER_PATH, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
                log.info("init /server");
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }


}
