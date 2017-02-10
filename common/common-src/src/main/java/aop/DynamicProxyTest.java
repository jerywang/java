/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * $Id DynamicProxyTest.java Sep 28,2016 wangguoxing (554952580@qq.com) $
 */
public class DynamicProxyTest {
    static interface IHello {
        public String sayHello();
    }

    static class DynaProxy implements InvocationHandler {
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 并未实现IHello,所以在DynaProxy中的invoke方法中不能调用method.invoke(Object obj, Object[] args),
            // 因为Java的动态代理机制要求obj上面的method调用的前提是obj实现了接口。
            // 此处虽然用了反射中的动态代理结构来组织代码实际上，此例子并非动态代理，这样就做到了IHello在没有任何实现的情况下，获得返回值
            System.out.println("dynamic proxy");
            return new String("123");
        }
    }

    public static void main(String[] args) {
        IHello hello = (IHello) Proxy
                .newProxyInstance(IHello.class.getClassLoader(), new Class[] {IHello.class}, new DynaProxy());
        String str = hello.sayHello();
        System.out.println(str);
    }
}
