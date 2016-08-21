/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.netty.rpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.baidu.netty.rpc.data.RpcRequest;
import com.baidu.netty.rpc.data.RpcResponse;
import com.baidu.netty.rpc.transport.Connection;
import com.baidu.netty.rpc.transport.RpcClientOptions;
import com.baidu.netty.rpc.zookeeper.ServiceDiscovery;

import io.netty.channel.ChannelFuture;
import lombok.extern.log4j.Log4j;

/**
 * RpcProxy.java 12 May 2016 wangguoxing
 * <p>
 * Description:
 */
@Log4j
public class RpcProxy implements InvocationHandler {

    private RpcChannel rpcChannel;
    private ServiceDiscovery serviceDiscovery;
    private Map<String, RpcClientHandler> rpcClientMap = new HashMap<String, RpcClientHandler>();
    private Map<String, Connection> rpcConnectionMap = new HashMap<String, Connection>();

    public RpcProxy(String host, int port) throws Exception {
        this.rpcChannel = new RpcChannel(host, port);
    }

    public RpcProxy(ServiceDiscovery serviceDiscovery) throws Exception {
        String serverAddress = serviceDiscovery.discover();
        if (serverAddress == null) {
            log.error("can not discover host");
        } else {
            String[] array = serverAddress.split(":");
            String host = array[0];
            int port = Integer.valueOf(array[1]);
            this.rpcChannel = new RpcChannel(host, port);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T create(Class<?> interfaceClass) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] {interfaceClass}, this);
    }

    public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);

        RpcResponse response = send(request); // 通过 RPC 客户端发送 RPC 请求并获取 RPC 响应
        log.info("request: " + JSON.toJSONString(request) + " response: " + JSON.toJSONString
                (response));
        return response.getResult();
    }

    private RpcResponse send(RpcRequest request) throws Exception {
        /////////
        Connection connection = new Connection(rpcChannel);
        InetSocketAddress address;
        address = new InetSocketAddress("127.0.0.1", 8000);
        ChannelFuture future1 = rpcChannel.connect(address).sync();
        // Wait until the connection is made successfully.
        future1.awaitUninterruptibly();
        if (!future1.isSuccess()) {
            log.error("failed to get result from stp", future1.cause());
        } else {
            connection.getIsConnected().set(true);
        }
        connection.setFuture(future1);
        //////////
        String connectionKey = rpcChannel.getConnection().toString();
        if (!rpcConnectionMap.containsKey(connectionKey)) {
            rpcConnectionMap.put(connectionKey, rpcChannel.getConnection());
        }

        ChannelFuture future = rpcConnectionMap.get(connectionKey).getFuture();
        future.channel().writeAndFlush(request).sync();

        Object obj = rpcChannel.getRpcClientHandler().getObj();
//        synchronized(obj) {
            obj.wait(); // 未收到响应，使线程等待
//        }

        if (rpcChannel.getRpcClientHandler().getResponse() != null) {
            future.channel().closeFuture().sync();
        }
        return rpcChannel.getRpcClientHandler().getResponse();
    }

}
