/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.netty.rpc.zookeeper;

/**
 * Constant.java 12 May 2016 wangguoxing
 * <p>
 * Description:
 */
public class Constant {
    public static final int ZK_SESSION_TIMEOUT = 5000;

    public static final String ZK_REGISTRY_PATH = "/registry";
    public static final String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";
}
