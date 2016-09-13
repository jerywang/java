/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package design.strategy;

/**
 * $Id TravelStrategy.java Sep 13,2016 wangguoxing@baidu.com $
 */
public interface TravelStrategy {
    public void travel();
}

/**
 * 具体策略类(ConcreteStrategy)1：乘坐飞机
 */
class AirStrategy implements TravelStrategy {
    public void travel() {
        System.out.println("travel by Air");
    }
}

/**
 * 具体策略类(ConcreteStrategy)2：乘坐火车
 */
class TrainStrategy implements TravelStrategy {
    public void travel() {
        System.out.println("travel by Train");
    }
}

/**
 * 具体策略类(ConcreteStrategy)3：骑自行车
 */
class BicycleStrategy implements TravelStrategy {
    public void travel() {
        System.out.println("travel by Bicycle");
    }
}