package zookeeper.configure;

/**
 * Configure.java 26 Feb 2017 wangguoxing
 * <p/>
 * Description: 客户端实现动态加载规则
 */
public interface Configure {

    /**
     * 刷新配置, 底层通过注册的configure进行callback
     * @param data 节点数据
     */
    public void reload(String data);

    /**
     * @return 配置对象
     */
    public Object getConfig();

}
