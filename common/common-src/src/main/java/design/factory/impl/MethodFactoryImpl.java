/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package design.factory.impl;

import org.springframework.stereotype.Component;

import design.factory.MethodFactory;
import design.factory.ProductA;

/**
 * $Id MethodFactory.java 2016-07-01 14:59 wangguoxing (554952580@qq.com) $
 *
 * 工厂方法模式
 */
@Component
public class MethodFactoryImpl implements MethodFactory {

    private MethodFactoryImpl() {}

    @SuppressWarnings("unchecked")
    public <T extends ProductA> T factory(Class<T> c) {
        try {
            ProductA product = (ProductA) Class.forName(c.getName()).newInstance();
            return (T) product;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
