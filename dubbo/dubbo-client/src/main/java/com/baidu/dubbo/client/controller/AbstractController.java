/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.dubbo.client.controller;

import org.springframework.validation.BindingResult;

import com.baidu.dubbo.client.exception.CommonException;

/**
 * Created by wangguoxing on 16-4-10.
 * AbstractController.java
 */
public abstract class AbstractController {

    protected void checkBindingResult(BindingResult result) {
        if (result.hasErrors()) {
            throw new CommonException(result.getFieldError().getDefaultMessage());
        }
    }
}
