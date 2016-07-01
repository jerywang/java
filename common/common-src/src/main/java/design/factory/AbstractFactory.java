/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package design.factory;

/**
 * $Id AbstractFactory.java 2016-07-01 15:14 wangguoxing@baidu.com $
 *
 * 抽象工厂模式
 */
public interface AbstractFactory {

    public ProductA newProductA();

    public ProductB newProductB();

}
