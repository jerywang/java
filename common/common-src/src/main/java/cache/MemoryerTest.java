/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package cache;

/**
 * $Id MemoryerTest.java 2016-06-28 18:10 wangguoxing@baidu.com $
 */
public class MemoryerTest  {
    private static final Computable<String, String> c = new Computable<String, String>() {
        public String compute(String arg) {
            return arg + ": testValue";
        }
    };
    private static final Computable<String, String> cache = new Memoryer<String, String>(c);

    public static void main(String[] args) {
        try {
            String value = cache.compute("key");
            System.out.print(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
