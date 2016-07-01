/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package design;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import design.factory.AbstractFactory;
import design.factory.MethodFactory;
import design.factory.ProductA;
import design.factory.ProductB;
import design.factory.impl.ProductAImpl;

/**
 * $Id FactoryTest.java 2016-07-01 15:55 wangguoxing@baidu.com $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class FactoryTest {

    @Autowired
    public MethodFactory methodFactory;

    @Autowired
    public AbstractFactory abstractFactory;

    @Test
    public void testFactory() {
        // 工厂方法
        ProductA productA = methodFactory.factory(ProductAImpl.class);
        // 抽象工厂
        ProductB productB = abstractFactory.newProductB();
        System.out.println();
        System.out.println(productA);
        System.out.println(productB);
    }

}
