/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package design.observer;

import java.util.Stack;

/**
 * $Id Subject.java Sep 13,2016 wangguoxing (554952580@qq.com) $
 */
public class Subject {

    private Stack<Observer> stack = new Stack<Observer>();

    public void add(Observer observer) {
        stack.add(observer); // push()
    }

    public void del(Observer observer) {
        stack.remove(observer);  // pop()
    }

    public void operation() {
        System.out.println("update self!");
        notifyObservers();
    }

    public void notifyObservers() {
        for (Observer observer : stack) {
            observer.update();
        }
    }

    public static void main(String[] args) {
        Subject subject = new Subject();
        subject.add(new Observer1());
        subject.add(new Observer2());
        subject.operation();
    }

}
