/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package zookeeper.test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * TestCreateTmp.java 27 Feb 2017 wangguoxing
 * <p>
 * Description:
 */
public class TestCreateTmp {
    public static void main(String[] args) throws Exception {
        final ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", Constants.ZK_SESSION_TIMEOUT, new Watcher() {
            public void process(WatchedEvent event) {
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println("connect zookeeper success!");
                }
            }
        });;
        final String data = "123456789";
        // 临时节点 - 不能重复
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i=0;i<10;i++) {
            executorService.execute(new Runnable() {
                public void run() {
                    try {
                        zooKeeper.create("/tmp", data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                        System.out.println("create zookeeper node: /tmp/"  + data);
                    } catch (KeeperException e) {
                        System.out.println("KeeperException" + e.getMessage());
                    } catch (InterruptedException ie) {
                        System.out.println("InterruptedException" + ie.getMessage());
                    }
                }
            });
        }
        executorService.shutdown();
        Thread.sleep(100);
        List<String> children =  zooKeeper.getChildren("/", false);
        System.out.println("getChildren:"+children);

        String node = new String(zooKeeper.getData("/tmp", false, new Stat()));
        System.out.println("/tmp data:" + node);


        // 临时自增节点 - 每次生成自增id
        Stat stat = zooKeeper.exists("/tmp", false);
        if (stat != null) {
            zooKeeper.delete("/tmp", stat.getVersion());
        }
        zooKeeper.create("/tmp", data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode
                .PERSISTENT);

        ExecutorService executorService2 = Executors.newFixedThreadPool(10);
        for (int i=0;i<10;i++) {
            executorService2.execute(new Runnable() {
                public void run() {
                    try {
                        zooKeeper.create("/tmp/list", data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode
                                .EPHEMERAL_SEQUENTIAL);
                        System.out.println("create zookeeper node: /tmp/list"  + data);
                    } catch (KeeperException e) {
                        System.out.println("KeeperException" + e.getMessage());
                    } catch (InterruptedException ie) {
                        System.out.println("InterruptedException" + ie.getMessage());
                    }
                }
            });
        }
        executorService2.shutdown();
        Thread.sleep(100);
        List<String> children2 =  zooKeeper.getChildren("/tmp", false);
        System.out.println("getChildren:"+children2);
        for (String str : children2) {
            System.out.println(str + ": " + new String(zooKeeper.getData("/tmp/"+str, false, new Stat())));
        }
    }


}
