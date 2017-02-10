/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.dubbo.client.request;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

/**
 * $Id DemoRequest.java 2016-04-07 20:59 wangguoxing $
 */
@Data
public class DemoRequest {
    @NotEmpty(message = "name不能为空")
    private String name;
}
