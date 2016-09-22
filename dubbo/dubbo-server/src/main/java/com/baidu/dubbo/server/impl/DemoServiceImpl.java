/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.dubbo.server.impl;

import org.springframework.stereotype.Service;

import com.baidu.dubbo.server.DemoService;

/**
 * $Id DemoServiceImpl.java Sep 19,2016 wangguoxing@baidu.com $
 */
@Service
public class DemoServiceImpl implements DemoService {
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
