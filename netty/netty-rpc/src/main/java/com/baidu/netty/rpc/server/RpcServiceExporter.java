/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.netty.rpc.server;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.baidu.netty.rpc.zookeeper.ServiceRegistry;

/**
 * RpcServiceExporter.java 26 May 2016 wangguoxing
 * <p>
 * Description:
 */
public class RpcServiceExporter implements InitializingBean, DisposableBean {
    private RpcServer rpcServer;
    private String serverAddress;
    private ServiceRegistry serviceRegistry;
    /**
     * register services
     */
    private List<Object> registerServices;

    public RpcServiceExporter(String serverAddress, List<Object> registerServices, ServiceRegistry serviceRegistry) {
        this.serverAddress = serverAddress;
        this.serviceRegistry = serviceRegistry;
        this.registerServices = registerServices;
    }

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.beans.factory.DisposableBean#destroy()
     */
    @Override
    public void destroy() throws Exception {
        if (rpcServer != null) {
            rpcServer.shutdown();
        }

        if (serviceRegistry != null && registerServices != null) {
            registerServices.clear();
            serviceRegistry.unRegister();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.rpcServer = new RpcServer(serviceRegistry);
        if (StringUtils.isBlank(serverAddress)) {
            rpcServer.start(new InetSocketAddress(1301));
        } else {
            String[] registerHost = serverAddress.split(":");
            String host = registerHost[0];
            int port = Integer.parseInt(registerHost[1]);
            rpcServer.start(new InetSocketAddress(host, port));
        }
        if (serviceRegistry != null) {
            serviceRegistry.register(serverAddress, registerServices);
        }

        ApplicationContextAware contextAware = new ApplicationContextAware() {
            @Override
            public void setApplicationContext(ApplicationContext ctx) throws BeansException {
                // 获取所有带有 RpcService 注解的 Spring Bean
                Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(RpcService.class);
                if (MapUtils.isNotEmpty(serviceBeanMap)) {
                    List<Object> services = new ArrayList<Object>();
                    for (Object service : serviceBeanMap.values()) {
                        services.add(service);
                    }
                    serviceRegistry.register(serverAddress, services);
                }
            }
        };
        contextAware = null;
    }
}
