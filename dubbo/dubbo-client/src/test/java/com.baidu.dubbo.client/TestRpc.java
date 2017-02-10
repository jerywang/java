/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.dubbo.client;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baidu.dubbo.server.DemoService;

import lombok.extern.log4j.Log4j;

/**
 * $Id TestRpc.java Sep 20,2016 wangguoxing $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-client.xml"})
@Log4j
public class TestRpc {
    @Resource
    private DemoService demoService;

    @Test
    public void testDemo() {
        Long startTime = System.currentTimeMillis();
        log.info("cost: " + (System.currentTimeMillis() - startTime));
        System.out.println(demoService.sayHello("jerry"));
    }
}
