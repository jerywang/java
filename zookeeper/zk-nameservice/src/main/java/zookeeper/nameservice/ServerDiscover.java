package zookeeper.nameservice;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * ServerDiscover.java 27 Feb 2017 wangguoxing
 * <p/>
 * Description: client 发现服务
 */
public class ServerDiscover implements ApplicationListener<ContextRefreshedEvent> {

    private ZooKeeper zk;

    private String connectString;

    private ServerWrapper serverWrapper;

    private static final Logger log = LoggerFactory.getLogger(ServerDiscover.class);

    public ServerWrapper getServerWrapper() {
        return serverWrapper;
    }

    public ServerDiscover(String connectString, ServerWrapper serverWrapper) {
        this.connectString = connectString;
        this.serverWrapper = serverWrapper;
    }

    public void discover() {
        try {
            this.zk = new ZooKeeper(connectString, Constants.ZK_SESSION_TIMEOUT, new Watcher() {
                public void process(WatchedEvent event) {
                    if (event.getType() == Event.EventType.NodeChildrenChanged
                            && Constants.ZK_SERVER_PATH.equals(event.getPath())) {
                        updateServerList();
                    }
                }
            });
        } catch (Exception e) {
            log.error("connect zookeeper failed: ", e);
        }
        updateServerList();
    }

    private void updateServerList() {
        try {
            List<String> newServerList = new ArrayList<String>();
            List<String> subList = zk.getChildren(Constants.ZK_SERVER_PATH, true);
            for (String subNode : subList) {
                byte[] data = zk.getData(Constants.ZK_SERVER_PATH + "/" + subNode, false, new Stat());
                newServerList.add(new String(data, "utf-8"));
            }
            serverWrapper.setServerList(newServerList);
            log.info("server list updated: " + newServerList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 开始监听节点
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            public void run() {
                discover();
                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        executor.shutdown();
    }

}
