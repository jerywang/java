/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.netty.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * RpcBootstrap.java 12 May 2016 wangguoxing
 * <p>
 * Description: 启动rpc服务
 */
public class RpcBootstrap {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("applicationContext-web.xml");
    }
}