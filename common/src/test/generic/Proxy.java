/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package test.generic;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 委托类属于实现类
 *
 * Created by wangguoxing on 15-8-6.
 */
public class Proxy {

    public Proxy(Base impl) {
        impl.execute();
    }

    // 或者
//    public <T extends Base> Proxy(T impl) {
//        impl.execute();
//    }

    @Autowired
    public void run(Base impl) {
        impl.execute();
    }

    public static void main(String args[]) {
        Base impl = new Impl();
        Proxy proxy = new Proxy(impl);
        proxy.run(impl);
    }
}
