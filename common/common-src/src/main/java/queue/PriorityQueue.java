/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package queue;

import java.util.Comparator;
import java.util.Queue;

/**
 * $Id PriorityQueueTest.java Oct 20,2016 wangguoxing@baidu.com $
 *
 * 优先队列
 */
public class PriorityQueue {
    private String name;
    private int population;

    public PriorityQueue(String name, int population) {
        this.name = name;
        this.population = population;
    }

    public String getName() {
        return this.name;
    }

    public int getPopulation() {
        return this.population;
    }

    public String toString() {
        return getName() + " - " + getPopulation();
    }

    public static void main(String args[]) {
        Comparator<PriorityQueue> OrderInfo = new Comparator<PriorityQueue>() {
            public int compare(PriorityQueue o1, PriorityQueue o2) {
                int number1 = o1.getPopulation();
                int number2 = o2.getPopulation();
                if (number2 > number1) {
                    return 1;
                } else if (number2 < number1) {
                    return -1;
                } else {
                    return 0;
                }
            }
        };

        Queue<PriorityQueue> priorityQueue = new java.util.PriorityQueue<PriorityQueue>(11, OrderInfo);

        PriorityQueue t1 = new PriorityQueue("t1", 1);
        PriorityQueue t3 = new PriorityQueue("t3", 3);
        PriorityQueue t2 = new PriorityQueue("t2", 2);
        PriorityQueue t4 = new PriorityQueue("t4", 0);
        priorityQueue.add(t1);
        priorityQueue.add(t3);
        priorityQueue.add(t2);
        priorityQueue.add(t4);
        System.out.println(priorityQueue.poll().toString());
    }
}
