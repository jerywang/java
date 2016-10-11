/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package dubbo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * $Id Provider.java Sep 19,2016 wangguoxing@baidu.com $
 */
public class Provider {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:provider.xml");
        context.start();

        System.in.read(); // 为保证服务一直开着，利用输入流的阻塞来模拟 按任意键退出
    }
}
