/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package design.factory.impl;

import design.factory.ProductA;

/**
 * $Id MethodFactory.java 2016-07-01 14:59 wangguoxing@baidu.com $
 *
 * 工厂方法模式
 */
public class MethodFactoryImpl {

    private MethodFactoryImpl() {}

    @SuppressWarnings("unchecked")
    public static <T extends ProductA> T factory(Class<T> c) {
        try {
            ProductA product = (ProductA) Class.forName(c.getName()).newInstance();
            return (T) product;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        ProductA product = MethodFactoryImpl.factory(ProductAImpl.class);
        System.out.println(product);
    }
}
