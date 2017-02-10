/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.dubbo.client.response;

import lombok.Data;

/**
 * $Id CommonResponse.java 2016-04-08 14:22 wangguoxing $
 */
@Data
public class CommonResponse {

    public static final int SUCCESS_CODE = 0;
    public static final String SUCCESS_MESSAGE = "成功";

    private int code;
    private String message;
    private Object result;

    /**
     * 默认构造一个成功、不带有结果的前端返回
     */
    public CommonResponse() {
        this(null);
    }

    /**
     * 构造一个成功的前端返回信息
     *
     * @param result 结果
     */
    public CommonResponse(Object result) {
        this(SUCCESS_CODE, SUCCESS_MESSAGE, result);
    }

    /**
     * 构造一个带有状态与信息的前端返回
     *
     * @param code 状态
     * @param message 信息
     */
    public CommonResponse(int code, String message) {
        this(code, message, null);
    }

    /**
     * 完整构造一个前端返回。
     *
     * @param code 状态
     * @param message 信息
     * @param result 结果
     */
    public CommonResponse(int code, String message, Object result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }
}
