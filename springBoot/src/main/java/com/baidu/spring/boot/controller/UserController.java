/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.spring.boot.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baidu.spring.boot.model.User;

/**
 * $Id UserController.java 2016-07-08 17:28 wangguoxing $
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @RequestMapping("/{id}")
    public User view(@PathVariable("id") int id) {
        User user = new User();
        user.setId(id);
        user.setName("jerry");
        return user;
    }
}
