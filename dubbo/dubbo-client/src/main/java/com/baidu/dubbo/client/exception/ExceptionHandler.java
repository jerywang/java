/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.dubbo.client.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.baidu.dubbo.client.response.CommonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j;

/**
 * Created by wangguoxing on 16-4-10.
 * <p/>
 * 捕获异常,并记录error log
 */
@Log4j
public class ExceptionHandler implements HandlerInterceptor {

    private final CommonResponse internalErrorResponse = new CommonResponse(1, "服务器内部错误");
    private String contentType = new MediaType("application", "json", Charset.forName("UTF-8")).toString();
    private ObjectMapper objectMapper = new ObjectMapper();

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception e) throws Exception {
        if (e == null) {
            return; // ignore if no exceptions
        }
        if (!(handler instanceof HandlerMethod)) {
            return;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        log.warn("exception message: " + e.getMessage(), e);
        if (e instanceof CommonException) {
            handleCommonException(request, response, handlerMethod, (CommonException) e);
            return;
        }

        handleOtherExceptions(request, response, handlerMethod, e);
    }

    public void handleCommonException(HttpServletRequest request, HttpServletResponse response,
                                      HandlerMethod handlerMethod, CommonException e) throws IOException {
        response.setContentType(contentType);
        PrintWriter out = response.getWriter();
        out.write(JSON.toJSONString(e.toCommonResponse()));
        return;
    }

    public void handleOtherExceptions(HttpServletRequest request, HttpServletResponse response,
                                      HandlerMethod handlerMethod, Exception e) throws IOException {
        log.error("An exception occurred when executing the method: " + handlerMethod, e);
        response.setContentType(contentType);
        objectMapper.writeValue(response.getOutputStream(), internalErrorResponse);
    }

}
