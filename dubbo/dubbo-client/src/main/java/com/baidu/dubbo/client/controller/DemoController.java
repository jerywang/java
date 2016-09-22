/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.dubbo.client.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.dubbo.client.request.DemoRequest;
import com.baidu.dubbo.client.response.CommonResponse;
import com.baidu.dubbo.server.DemoService;

/**
 * $Id DemoController.java 2016-04-07 20:58 wangguoxing@baidu.com $
 */
@Controller
@RequestMapping("/demo")
public class DemoController extends AbstractController {

    @Resource
    private DemoService demoService;

    @RequestMapping(value = "/sample", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public CommonResponse getUserInfo(@Valid DemoRequest request, BindingResult bindingResult) {
        checkBindingResult(bindingResult);
        return new CommonResponse(demoService.sayHello(request.getName()));
    }
}
