/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.netty.rpc.client;

import java.util.HashMap;
import java.util.Map;

import com.baidu.netty.rpc.data.RpcResponse;
import com.baidu.netty.rpc.transport.Connection;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.log4j.Log4j;

/**
 * RpcClientHandler.java 12 May 2016 wangguoxing
 * <p>
 * Description:
 */
@Log4j
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    private RpcResponse response;
    private final Object obj = new Object();
    private Map<String, Connection> rpcConnectionMap = new HashMap<String, Connection>();

    @Override
    public void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        this.response = response;

        synchronized(obj) {
            obj.notifyAll(); // 收到响应，唤醒线程
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("client caught exception", cause);
        ctx.close();
    }

    public Object getObj() {
        return this.obj;
    }

    public RpcResponse getResponse() {
        return this.response;
    }

}
