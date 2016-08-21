/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.netty.rpc.client;

import java.net.InetSocketAddress;

import com.baidu.netty.rpc.transport.ChannelPool;
import com.baidu.netty.rpc.transport.Connection;
import com.baidu.netty.rpc.transport.RpcClientOptions;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultMessageSizeEstimator;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.log4j.Log4j;

/**
 * RpcChannel.java 22 May 2016 wangguoxing
 * <p>
 * Description:
 */
@Log4j
public class RpcChannel {
    private RpcClientHandler rpcClientHandler;
    private Bootstrap bootstrap;
    private RpcClientOptions rpcClientOptions;
    private ChannelPool channelPool;

    public RpcChannel(String host, int port) throws Exception {
        this.rpcClientOptions = new RpcClientOptions();
        this.rpcClientHandler = new RpcClientHandler();
        this.bootstrap = getBootStrap();
        this.channelPool = new ChannelPool(this, host, port);
    }

    public RpcClientOptions getRpcClientOptions() {
        return this.rpcClientOptions;
    }

    public RpcClientHandler getRpcClientHandler() {
        return this.rpcClientHandler;
    }

    public Connection getConnection() {
        return channelPool.getChannel();
    }

    private Bootstrap getBootStrap() {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup(rpcClientOptions.getThreadPoolSize());
        bootstrap.group(group).channel(NioSocketChannel.class)
                .handler(rpcClientHandler)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_REUSEADDR, rpcClientOptions.isReuseAddress())
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, rpcClientOptions.getConnectTimeout())
                .option(ChannelOption.SO_SNDBUF, rpcClientOptions.getSendBufferSize())
                .option(ChannelOption.SO_RCVBUF, rpcClientOptions.getSendBufferSize())
                .option(ChannelOption.SO_KEEPALIVE, rpcClientOptions.isKeepAlive())
                .option(ChannelOption.TCP_NODELAY, rpcClientOptions.isTcpNoDelay())
                .option(ChannelOption.MESSAGE_SIZE_ESTIMATOR,
                        new DefaultMessageSizeEstimator(rpcClientOptions.getReceiveBufferSize()));

        return bootstrap;
    }

    public ChannelFuture connect(InetSocketAddress address) {
        return bootstrap.connect(address);
    }

}
