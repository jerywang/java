/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.netty.rpc.data;

import lombok.Data;

/**
 * RpcRequest.java 12 May 2016 wangguoxing
 * <p>
 * Description:
 */
@Data
public class RpcRequest {
    private String requestId;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;

}
