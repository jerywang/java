/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.netty.server.vo;

import java.util.Date;

import lombok.Data;

/**
 * HelloRequest.java 21 May 2016 wangguoxing
 * <p>
 * Description:
 */
@Data
public class HelloRequest {
    private int uid;
    private String name;
    private Date date = new Date();
}
