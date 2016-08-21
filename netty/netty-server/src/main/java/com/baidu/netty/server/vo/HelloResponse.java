/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.netty.server.vo;

import lombok.Data;

/**
 * HelloResponse.java 21 May 2016 wangguoxing
 * <p>
 * Description:
 */
@Data
public class HelloResponse {
    private int code = 0;
    private String message = "success";
    private HelloRequest request;
}
