/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package design.builder;

/**
 * $Id Builder.java Sep 12,2016 wangguoxing (554952580@qq.com) $
 */
public interface Builder {
    public void buildPart1();

    public void buildPart2();

    public Product retrieveResult();
}
