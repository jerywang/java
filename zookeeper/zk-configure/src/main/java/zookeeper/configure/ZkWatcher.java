package zookeeper.configure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

import com.google.common.collect.Lists;

/**
 * ZkWatcher.java 26 Feb 2017 wangguoxing
 * <p/>
 * Description: client 监听配置
 */
public class ZkWatcher implements ApplicationListener<ContextRefreshedEvent> {

    private ZooKeeper zk;

    private String connectString;

    private String path;

    private int sessionTimeout = 5000;

    private volatile List<Configure> configureList = new ArrayList<Configure>();

    private static final Logger log = LoggerFactory.getLogger(ZkWatcher.class);

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public String getConnectString() {
        return connectString;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setSessionTimeout(int timeout) {
        this.sessionTimeout = timeout;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setConfigureList(List<Configure> configures) {
        for (Configure configure : configures) {
            if (!this.configureList.contains(configure)) {
                this.configureList.add(configure);
            }
        }
    }

    public List<Configure> getConfigureList() {
        return configureList;
    }

    public void watcher() {
        try {
            this.zk = new ZooKeeper(getConnectString(), getSessionTimeout(), new Watcher() {
                public void process(WatchedEvent event) {
                    log.info("zookeeper " + event.getPath() + " is changed: " + event.getType().name());
                }
            });
            updateConfigureList();
            //设置监听器
            Watcher wc = new Watcher() {
                public void process(WatchedEvent event) {
                    if (event.getType() == Event.EventType.NodeDataChanged) {
                        updateConfigureList();
                    }
                }
            };
            //进行轮询，其中exists方法用来询问状态，并且设置了监听器，如果发生变化，则会回调监听器里的方法。
            while (true) {
                zk.exists(getPath(), wc);
                Thread.sleep(10L);
            }
        } catch (Exception e) {
            log.error("connect zookeeper failed: ", e);
        }
    }

    private void updateConfigureList() {
        try {
            byte[] data = zk.getData(getPath(), false, new Stat());
            String str = new String(data, "utf-8");
            log.info(getPath() + " is updated: " + str);
            for (Configure configure : getConfigureList()) {
                configure.reload(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 注册Configure
        Map<String, Configure> beanMap = event.getApplicationContext().getBeansOfType(Configure.class);
        for (String beanName : beanMap.keySet()) {
            Configure configure = beanMap.get(beanName);
            setConfigureList(Lists.newArrayList(configure));
        }
        // 开始监听节点
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            public void run() {
                watcher();
            }
        });
        executor.shutdown();
    }

}
