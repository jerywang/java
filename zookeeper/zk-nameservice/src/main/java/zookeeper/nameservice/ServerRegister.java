package zookeeper.nameservice;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ServerRegister.java 27 Feb 2017 wangguoxing
 * <p/>
 * Description: register host port, services
 */
public class ServerRegister {

    private ZooKeeper zk;

    private static final Logger log = LoggerFactory.getLogger(ServerRegister.class);

    public ServerRegister(String host, int timeout) {
        try {
            this.zk = new ZooKeeper(host, timeout, new Watcher() {
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        log.info("connect zookeeper success!");
                    }
                }
            });
        } catch (Exception e) {
            log.error("connect zookeeper failed: ", e);
        }
    }

    public void create(String data) {
        try {
            // 子节点的类型设置为EPHEMERAL_SEQUENTIAL, 表明这是一个临时节点, 且在子节点的名称后面加上一串数字后缀
            zk.create(Constants.ZK_SERVER_DATA_PATH, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);
            log.info("create zookeeper node: " + Constants.ZK_SERVER_DATA_PATH + data);
        } catch (Exception e) {
            log.error("create zookeeper node failed:", e);
        }
    }

}
