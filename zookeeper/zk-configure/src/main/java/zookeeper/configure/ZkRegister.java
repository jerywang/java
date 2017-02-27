package zookeeper.configure;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZkRegister.java 26 Feb 2017 wangguoxing
 * <p/>
 * Description: register path
 */
public class ZkRegister {

    private ZooKeeper zk;

    private static final Logger log = LoggerFactory.getLogger(ZkRegister.class);

    public ZkRegister(String host, int timeout) {
        try {
            zk = new ZooKeeper(host, timeout, new Watcher() {
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

    public void createNode(String path, String data) {
        try {
            zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNode(String path, String data) {
        try {
            Stat stat = zk.exists(path, false);
            if (stat != null) {
                zk.setData(path, data.getBytes(), stat.getVersion());
                log.info("update zookeeper node success: " + path + "/" + data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createOrSetNode(String path, String data) {
        try {
            Stat stat = zk.exists(path, false);
            if (stat == null) {
                zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                log.info("create zookeeper node success: " + path + "/" + data);
            } else {
                zk.setData(path, data.getBytes(), stat.getVersion());
                log.info("update zookeeper node success: " + path + "/" + data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
