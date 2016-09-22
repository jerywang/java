/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.dubbo.client.exception;

import com.baidu.dubbo.client.response.CommonResponse;

/**
 * Created by wangguoxing on 16-4-10.
 *
 * 前端通用异常类
 */
public class CommonException extends RuntimeException {

    public static final int code = 1;
    private String message;

    public CommonException(String message) {
        super(message);
        this.message = message;

    }

    public CommonResponse toCommonResponse() {
        return new CommonResponse(code, message);
    }

}
