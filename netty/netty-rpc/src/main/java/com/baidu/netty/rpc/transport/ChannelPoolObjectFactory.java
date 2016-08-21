/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.netty.rpc.transport;

import java.net.InetSocketAddress;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.baidu.netty.rpc.client.RpcChannel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.extern.log4j.Log4j;

/**
 * ChannelPoolObjectFactory.java 22 May 2016 wangguoxing
 * <p>
 * Description:
 */
@Log4j
public class ChannelPoolObjectFactory extends BasePooledObjectFactory<Connection> {
    private final RpcChannel rpcChannel;
    private final String host;
    private final int port;

    public ChannelPoolObjectFactory(RpcChannel rpcChannel, String host, int port) {
        super();
        this.rpcChannel = rpcChannel;
        this.host = host;
        this.port = port;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.commons.pool2.BasePooledObjectFactory#create()
     */
    @Override
    public Connection create() throws Exception {
        Connection connection = new Connection(rpcChannel);
        InetSocketAddress address;
        if (host == null) {
            address = new InetSocketAddress(port);
        } else {
            address = new InetSocketAddress(host, port);
        }
        try {
            ChannelFuture future = rpcChannel.connect(address).sync();
            // Wait until the connection is made successfully.
            future.awaitUninterruptibly();
            if (!future.isSuccess()) {
                log.error("failed to get result from stp", future.cause());
            } else {
                connection.getIsConnected().set(true);
            }
            connection.setFuture(future);
        } catch (Exception e) {
            log.error(e);
        }
        return connection;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.commons.pool2.BasePooledObjectFactory#wrap(java.lang.Object)
     */
    @Override
    public PooledObject<Connection> wrap(Connection obj) {
        return new DefaultPooledObject<Connection>(obj);
    }

    public void destroyObject(PooledObject<Connection> p) throws Exception {
        Connection c = p.getObject();
        Channel channel = c.getFuture().channel();
        if (channel.isOpen() && channel.isActive()) {
            channel.close();
        }
    }

    public boolean validateObject(PooledObject<Connection> p) {
        Connection c = p.getObject();
        Channel channel = c.getFuture().channel();
        return channel.isOpen() && channel.isActive();

    }

    /**
     * activateObject will invoke every time before it borrow from the pool
     *
     * @param p target pool object
     *
     * @throws Exception
     */
    public void activateObject(PooledObject<Connection> p) throws Exception {
    }

    /**
     * is invoked on every instance when it is returned to the pool.
     *
     * @param p target pool object
     *
     * @throws Exception
     */
    public void passivateObject(PooledObject<Connection> p) throws Exception {
        p.getObject().clearRequests();
    }

}
