/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.netty.server.api;

import com.baidu.netty.server.vo.HelloRequest;
import com.baidu.netty.server.vo.HelloResponse;

/**
 * HelloService.java 21 May 2016 wangguoxing
 * <p>
 * Description:
 */
public interface HelloService {
    public HelloResponse hello(HelloRequest request);
}
