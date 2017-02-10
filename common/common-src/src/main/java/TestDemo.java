/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
import java.util.Arrays;
import java.util.List;

import com.baidu.noah.naming.BNSClient;
import com.baidu.noah.naming.BNSInstance;

import util.JsonUtil;

/**
 * $Id TestDemo.java Jan 11,2017 wangguoxing (554952580@qq.com) $
 */
public class TestDemo {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<String>();

    public static String doString(String... args0) {
        StringBuilder sb = new StringBuilder();
        for (String str : args0) {
            sb.append(str);
        }

        return sb.toString();
    }

    public static List<BNSInstance> getInstanceByService(String serviceName) {
        BNSClient bnsClient = new BNSClient();
        return bnsClient.getInstanceByService(serviceName);
    }

    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());
        System.out.println(Runtime.getRuntime().totalMemory()/(10240*1024));
//        System.out.println(TestDemo.doString("a","b", "c"));
//        System.out.println(TestDemo.doString(new String[] {"a", "b", "c"}));
//
//        threadLocal.set("abc");
//        System.out.println(threadLocal.get());
//
//        System.out.println(JsonUtil.toJson(getInstanceByService("group.es-kuai.BUS.cn")));
    }
}
