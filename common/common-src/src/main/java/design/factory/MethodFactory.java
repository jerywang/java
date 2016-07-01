/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package design.factory;

/**
 * $Id MethodFactory.java 2016-07-01 14:59 wangguoxing@baidu.com $
 *
 * 工厂方法模式
 */
public class MethodFactory {

    private MethodFactory() {}

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

}
