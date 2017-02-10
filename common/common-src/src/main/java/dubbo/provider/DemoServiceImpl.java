/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package dubbo.provider;

import dubbo.DemoService;

/**
 * $Id DemoServiceImpl.java Sep 19,2016 wangguoxing (554952580@qq.com) $
 */
public class DemoServiceImpl implements DemoService {
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
