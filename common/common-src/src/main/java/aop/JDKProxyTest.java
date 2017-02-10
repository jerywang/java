/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package aop;

/**
 * $Id JDKProxyTest.java Aug 11,2016 wangguoxing (554952580@qq.com) $
 */
public class JDKProxyTest {
    public static void main(String[] args) {
        JDKProxy jdkProxy = new JDKProxy();
        UserManager userManager = (UserManager) jdkProxy.newProxy(new UserManagerImpl());
        userManager.addUser("jerry", "root");
        userManager.delUser("jerry");
    }
}
