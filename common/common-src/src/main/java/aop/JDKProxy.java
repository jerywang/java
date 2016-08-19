/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * $Id JDKProxy.java Aug 11,2016 wangguoxing@baidu.com $
 */
public class JDKProxy implements InvocationHandler {

    private Object targetObject;//需要代理的目标对象

    public Object newProxy(Object targetObject) {//将目标对象传入进行代理
        this.targetObject = targetObject;
        return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(),
                targetObject.getClass().getInterfaces(), this);//返回代理对象
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before call");
        Object ret = method.invoke(targetObject, args); //调用invoke方法，ret存储该方法的返回值
        if("addUser".equals(method.getName())) {
            System.out.print("args: ");
            for (Object str: args) {
                System.out.print(str + " ");
            }
            System.out.println("\n after call addUser");
        }
        return ret;
    }

}
