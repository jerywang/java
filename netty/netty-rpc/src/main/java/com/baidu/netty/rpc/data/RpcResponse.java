/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.netty.rpc.data;

import lombok.Data;

/**
 * RpcResponse.java 12 May 2016 wangguoxing
 * <p>
 * Description:
 */
@Data
public class RpcResponse {
    private String requestId;
    private Throwable error;
    private Object result;
}
