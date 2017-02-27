/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package zookeeper.test.config;

import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * $Id RedisConfig.java Feb 27,2017 wangguoxing@baidu.com $
 */
@Data
@Component
public class RedisConfig {
    private String host;
    private int port;
}
