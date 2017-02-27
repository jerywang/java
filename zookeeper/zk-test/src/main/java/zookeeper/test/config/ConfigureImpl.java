/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package zookeeper.test.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import zookeeper.configure.Configure;
import zookeeper.test.util.JsonUtil;

/**
 * $Id ConfigureImpl.java Feb 27,2017 wangguoxing@baidu.com $
 */
public class ConfigureImpl implements Configure {

    @Autowired
    private RedisConfig redisConfig;

    public void reload(String data) {
        try {
            RedisConfig redis = JsonUtil.fromJson(data, RedisConfig.class);
            redisConfig.setHost(redis.getHost());
            redisConfig.setPort(redis.getPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("after reload redis config is:" + redisConfig);
    }

    public Object getConfig() {
        return redisConfig;
    }
}