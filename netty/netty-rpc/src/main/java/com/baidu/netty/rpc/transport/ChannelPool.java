/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.netty.rpc.transport;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.alibaba.fastjson.JSON;
import com.baidu.netty.rpc.client.RpcChannel;

import io.netty.bootstrap.Bootstrap;
import lombok.extern.log4j.Log4j;

/**
 * ChannelPool.java 22 May 2016 wangguoxing
 * <p>
 * Description:
 */
@Log4j
public class ChannelPool {
    private final RpcClientOptions clientConfig;
    private final PooledObjectFactory<Connection> pooledObjectFactory;
    private final GenericObjectPool<Connection> pool;

    public ChannelPool(RpcChannel rpcChannel, String host, int port) throws Exception {
        this.clientConfig = rpcChannel.getRpcClientOptions();
        this.pooledObjectFactory = new ChannelPoolObjectFactory(rpcChannel, host, port);
        pooledObjectFactory.makeObject();
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setJmxEnabled(clientConfig.isJmxEnabled());
        config.setMaxIdle(clientConfig.getMaxIdleSize());
        config.setMaxTotal(clientConfig.getThreadPoolSize());
        config.setMaxWaitMillis(clientConfig.getMaxWait());
        config.setMinIdle(clientConfig.getMinIdleSize());
        config.setMinEvictableIdleTimeMillis(clientConfig.getMinEvictableIdleTime());
        config.setTestOnBorrow(clientConfig.isTestOnBorrow());
        config.setTestOnReturn(clientConfig.isTestOnReturn());
        config.setLifo(clientConfig.isLifo());
        this.pool = new GenericObjectPool<Connection>(pooledObjectFactory, config);
    }

    public Connection getChannel() {
        Connection channel = null;
        try {
            if (!clientConfig.isShortConnection()) {
                channel = pool.borrowObject();
            } else {
                channel = pooledObjectFactory.makeObject().getObject();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return channel;
    }

    public void stop() {
        try {
            if (pool != null) {
                pool.clear();
                pool.close();
            }
        } catch (Exception e) {
            log.error("stop channel failed!", e);
        }
    }
}
