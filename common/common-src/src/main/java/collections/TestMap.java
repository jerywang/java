/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package collections;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.collect.Maps;

/**
 * $Id TestMap.java Aug 18,2016 wangguoxing@baidu.com $
 * <p/>
 * hashMap无序
 * TreeMap有序 - 实现原理 红黑树
 * LinkedHashMap有序 - 实现原理 链表
 */
public class TestMap {
    public static void main(String[] args) {
        HashMap<Integer, String> hashMap = Maps.newHashMap();
        TreeMap<Integer, String> treeMap = Maps.newTreeMap();
        LinkedHashMap<Integer, String> linkedHashMap = Maps.newLinkedHashMap();

        hashMap.put(3, "33");
        hashMap.put(1, "11");
        hashMap.put(2, "22");

        treeMap.put(3, "33");
        treeMap.put(2, "22");
        treeMap.put(1, "11");

        linkedHashMap.put(3, "33");
        linkedHashMap.put(1, "11");
        linkedHashMap.put(2, "22");

        System.out.println("遍历HashMap: 通过Map.entrySet遍历key和value");
        for (Map.Entry<Integer, String> entry : hashMap.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }

        System.out.println("遍历TreeMap: 通过Map.entrySet遍历key和value");
        for (Map.Entry<Integer, String> entry : treeMap.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }

        System.out.println("遍历LinkedHashMap: 通过Map.entrySet遍历key和value");
        for (Map.Entry<Integer, String> entry : linkedHashMap.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }

    }
}
