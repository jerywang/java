/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package design.factory;

/**
 * $Id MethodFactory.java 2016-07-01 14:59 wangguoxing (554952580@qq.com) $
 *
 * 工厂方法模式
 */
public interface MethodFactory {

    public <T extends ProductA> T factory(Class<T> c);

}
