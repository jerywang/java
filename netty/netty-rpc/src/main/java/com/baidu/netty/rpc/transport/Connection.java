/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.netty.rpc.transport;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.baidu.netty.rpc.client.RpcChannel;
import com.baidu.netty.rpc.data.RpcRequest;

import io.netty.channel.ChannelFuture;
import lombok.Data;

/**
 * Connection.java 22 May 2016 wangguoxing
 * <p>
 * Description:
 */
@Data
public class Connection {
    /**
     * max request default count
     */
    private static final int MAX_REQUEST_SIZE = 102400;
    private AtomicBoolean isConnected = new AtomicBoolean();
    private BlockingQueue<RpcRequest> requestQueue;
    private ChannelFuture future;
    private RpcChannel rpcChannel;

    public Connection(RpcChannel rpcChannel) {
        this.isConnected.set(false);
        this.requestQueue = new ArrayBlockingQueue<RpcRequest>(MAX_REQUEST_SIZE);
        this.future = null;
        this.rpcChannel = rpcChannel;
    }
//
//    public boolean produceRequest(RpcClientCallState state) {
//        return requestQueue.add(state);
//    }
//
//    public RpcClientCallState consumeRequest() {
//        return requestQueue.poll();
//    }

    public void clearRequests() {
        requestQueue.clear();
    }
}
