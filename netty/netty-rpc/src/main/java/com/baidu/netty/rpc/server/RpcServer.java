/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.netty.rpc.server;

import java.net.InetSocketAddress;

import com.baidu.netty.rpc.data.RpcRequest;
import com.baidu.netty.rpc.data.RpcResponse;
import com.baidu.netty.rpc.util.RpcDecoder;
import com.baidu.netty.rpc.util.RpcEncoder;
import com.baidu.netty.rpc.zookeeper.ServiceRegistry;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;
import lombok.extern.log4j.Log4j;

/**
 * RpcServer.java 12 May 2016 wangguoxing
 * <p>
 * Description:
 */
@Log4j
@Data
public class RpcServer {

    private ChannelFuture future;
    private Channel channel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap serverBootstrap;
//    private ServiceRegistry serviceRegistry;

    public RpcServer(final ServiceRegistry serviceRegistry) throws Exception {
        if (serverBootstrap == null) {
            this.bossGroup = new NioEventLoopGroup();
            this.workerGroup = new NioEventLoopGroup();
            this.serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new RpcDecoder(RpcRequest.class)) // 将 RPC 请求进行解码（为了处理请求）
                                    .addLast(new RpcEncoder(RpcResponse.class)) // 将 RPC 响应进行编码（为了返回响应）
                                    .addLast(new RpcHandler(serviceRegistry.getServices())); // 处理 RPC 请求
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
        }
    }

    public void start(InetSocketAddress inetSocketAddress) throws Exception {
        future = serverBootstrap.bind(inetSocketAddress).sync();
        channel = future.channel();
        log.info("server started");
    }

    public void shutdown() {
        if (channel != null && channel.isOpen()) {
            channel.close();
        }

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

}