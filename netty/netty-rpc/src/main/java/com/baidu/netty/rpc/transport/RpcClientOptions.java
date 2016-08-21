/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */

package com.baidu.netty.rpc.transport;

import lombok.Data;

/**
 * RPC client properties.
 */
@Data
public class RpcClientOptions {

    private int connectTimeout; // connection time out in milliseconds
    private int sendBufferSize;
    private int receiveBufferSize;
    private boolean tcpNoDelay;
    private boolean keepAlive;
    private boolean reuseAddress;
    private int idleTimeout;

    // connection pool settings
    private boolean shortConnection = false;
    private int threadPoolSize = 20;
    private int maxIdleSize = 20;
    private int minIdleSize = 2;
    private long minEvictableIdleTime = 1000L * 60L * 2;
    private long maxWait = 2000L; // max wait time in milliseconds for pool available
    
    private boolean lifo = false;
    
    private boolean shareThreadPoolUnderEachProxy = false; // share a thread pool under each rpc proxy

    private boolean testOnBorrow = true;
    private boolean testOnReturn = false;

    // in MILLISECONDS unit
    private int onceTalkTimeout = 1000;

    // if use chunkSize will split chunkSize
    private long chunkSize = -1;
    
    private boolean jmxEnabled = false;

    public void copyFrom(RpcClientOptions options) {
        if (options == null) {
            return;
        }
        this.connectTimeout = options.connectTimeout;
        this.sendBufferSize = options.sendBufferSize;
        this.receiveBufferSize = options.receiveBufferSize;
        this.tcpNoDelay = options.tcpNoDelay;
        this.keepAlive = options.keepAlive;
        this.reuseAddress = options.reuseAddress;
        this.idleTimeout = options.idleTimeout;
        this.shortConnection = options.shortConnection;
        this.threadPoolSize = options.threadPoolSize;
        this.maxIdleSize = options.maxIdleSize;
        this.minIdleSize = options.minIdleSize;
        this.minEvictableIdleTime = options.minEvictableIdleTime;
        this.maxWait = options.maxWait;
        this.onceTalkTimeout = options.onceTalkTimeout;
        this.chunkSize = options.chunkSize;
        this.testOnBorrow = options.testOnBorrow;
        this.testOnReturn = options.testOnReturn;
        this.shareThreadPoolUnderEachProxy = options.shareThreadPoolUnderEachProxy;
        this.jmxEnabled = options.jmxEnabled;
        this.lifo = options.lifo;
    }

    /**
     * time out set for chunk package wait in ms.
     */
    private int chunkPackageTimeout = 300 * 1000;

    public RpcClientOptions() {
        this.connectTimeout = 1000;
        this.sendBufferSize = 1048576;
        this.receiveBufferSize = 1048576;
        this.keepAlive = true;
        this.tcpNoDelay = true;
        this.idleTimeout = 0;
        this.reuseAddress = true;
    }

}
