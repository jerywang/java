/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package design.builder;

/**
 * $Id Director.java Sep 12,2016 wangguoxing@baidu.com $
 */
public class Director {
    /**
     * 持有当前需要使用的建造器对象
     */
    private Builder builder;

    /**
     * 构造方法，传入建造器对象
     *
     * @param builder 建造器对象
     */
    public Director(Builder builder) {
        this.builder = builder;
    }

    /**
     * 产品构造方法，负责调用各个零件建造方法
     */
    public void construct() {
        builder.buildPart1();
        builder.buildPart2();
    }

    public static void main(String[] args) {
        Builder builder = new BuilderImpl();
        Director director = new Director(builder);
        director.construct();
        Product product = builder.retrieveResult();
        System.out.println(product);
    }
}
