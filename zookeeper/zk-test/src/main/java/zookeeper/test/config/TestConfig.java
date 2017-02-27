/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package zookeeper.test.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import zookeeper.configure.Configure;
import zookeeper.configure.ZkRegister;

/**
 * $Id TestConfig.java Feb 27,2017 wangguoxing@baidu.com $
 */
public class TestConfig {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        Configure configure = (Configure) ctx.getBean("configure");
        ZkRegister zkRegister = (ZkRegister) ctx.getBean("zkRegister");

        System.out.println(configure.getConfig());

        for (int i = 0; i<100; i++) {
            try {
                zkRegister.createOrSetNode("/redis", "{\"host\":\"127.0.0.1\",\"port\":" + i + "}");
                Thread.sleep(1000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
