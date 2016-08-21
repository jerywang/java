/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package netty.rpc;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baidu.netty.rpc.client.RpcProxy;
import com.baidu.netty.server.vo.HelloRequest;
import com.baidu.netty.server.vo.HelloResponse;
import com.baidu.netty.server.api.HelloService;

/**
 * HelloServiceTest.java 12 May 2016 wangguoxing
 * <p>
 * Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-client.xml")
public class HelloTest {

    @Resource
    private RpcProxy rpcProxy;

    @Test
    public void helloTest() throws Exception {
        HelloService helloService = rpcProxy.create(HelloService.class);
        for(int i = 1; i < 1000; i++) {
            long cost = -System.currentTimeMillis();
            HelloRequest request = new HelloRequest();
            request.setUid(i);
            request.setName("wangguoxing");
            HelloResponse result = helloService.hello(request);
            System.out.println(result);
            cost += System.currentTimeMillis();
            System.out.println("cost: " + cost);
        }
    }

}