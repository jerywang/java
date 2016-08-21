/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.netty.server.api.impl;

import com.baidu.netty.rpc.server.RpcService;
import com.baidu.netty.server.api.HelloService;
import com.baidu.netty.server.vo.HelloRequest;
import com.baidu.netty.server.vo.HelloResponse;

/**
 * HelloServiceImpl.java 21 May 2016 wangguoxing
 * <p>
 * Description:
 */
@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService {
    @Override
    public HelloResponse hello(HelloRequest request) {
        HelloResponse response = new HelloResponse();
        response.setRequest(request);
        return response;
    }
}
