/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package design.strategy;

/**
 * $Id PersonContext.java Sep 13,2016 wangguoxing@baidu.com $
 */
public class PersonContext {
    private TravelStrategy strategy = null;

    public PersonContext(TravelStrategy travel) {
        this.strategy = travel;
    }

    /**
     * 旅行
     */
    public void setTravelStrategy(TravelStrategy travel) {
        this.strategy = travel;
    }

    /**
     * 旅行
     */
    public void travel() {
        this.strategy.travel();
    }

    public static void main(String[] args) {
        // 乘坐火车旅行
        PersonContext context = new PersonContext(new TrainStrategy());
        context.travel();

        // 改骑自行车
        context.setTravelStrategy(new BicycleStrategy());
        context.travel();
    }
}
