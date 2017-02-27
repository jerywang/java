/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package zookeeper.test.nameservice;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import zookeeper.configure.Configure;
import zookeeper.configure.ZkRegister;
import zookeeper.nameservice.ServerRegister;
import zookeeper.nameservice.ServerWrapper;

/**
 * $Id TestName.java Feb 27,2017 wangguoxing@baidu.com $
 */
public class TestName {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        ServerWrapper serverWrapper = (ServerWrapper) ctx.getBean("serverWrapper");
        ServerRegister serverRegister = (ServerRegister) ctx.getBean("serverRegister");
        for (int i = 0; i<100; i++) {
            try {
                serverRegister.create("127.0.0." + i);
                System.out.println(serverWrapper.getServerList());
                Thread.sleep(1000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
