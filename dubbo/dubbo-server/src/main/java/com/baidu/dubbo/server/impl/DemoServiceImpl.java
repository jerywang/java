/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.dubbo.server.impl;

import org.springframework.stereotype.Service;

import com.baidu.dubbo.server.DemoService;

import lombok.extern.log4j.Log4j;

/**
 * $Id DemoServiceImpl.java Sep 19,2016 wangguoxing $
 */
@Service
@Log4j
public class DemoServiceImpl implements DemoService {
    public String sayHello(String name) {
        log.info("Hello " + name);
        return "Hello " + name;
    }
}
