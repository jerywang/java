/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import lombok.extern.log4j.Log4j;

/**
 * $Id Consumer.java Sep 19,2016 wangguoxing@baidu.com $
 */
@Log4j
public class Consumer {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:consumer.xml");
        //        context.start();

        DemoService demoService = (DemoService) context.getBean("demoService"); // 获取远程服务代理
        String hello = demoService.sayHello("world"); // 执行远程方法
        System.out.println("\n" + hello + "\n"); // 显示调用结果
    }
}
