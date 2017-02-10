/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package design.observer;

/**
 * $Id Observer.java Sep 13,2016 wangguoxing (554952580@qq.com) $
 */
public interface Observer {
    public void update();
}

class Observer1 implements Observer {

    @Override
    public void update() {
        System.out.println("observer1 has received!");
    }
}

class Observer2 implements Observer {

    @Override
    public void update() {
        System.out.println("observer2 has received!");
    }

}