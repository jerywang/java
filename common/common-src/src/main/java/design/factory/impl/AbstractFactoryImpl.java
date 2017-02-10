/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package design.factory.impl;

import org.springframework.stereotype.Component;

import design.factory.AbstractFactory;
import design.factory.ProductA;
import design.factory.ProductB;

/**
 * $Id AbstractFactory.java 2016-07-01 15:14 wangguoxing (554952580@qq.com) $
 *
 * 抽象工厂模式
 */
@Component
public class AbstractFactoryImpl implements AbstractFactory {

    private AbstractFactoryImpl() {}

    @Override
    public ProductA newProductA() {
        return new ProductAImpl();
    }

    @Override
    public ProductB newProductB() {
        return new ProductBImpl();
    }


}
