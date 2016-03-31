/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package thread;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 多线程返回执行结果
 *
 * Created by wangguoxing on 15-8-28.
 */
public class TaskWithResult implements Callable<String> {

    private int id;

    public TaskWithResult(int id) {
        this.id = id;
    }

    @Override
    public String call() throws Exception {
        return "result of TaskWithResult: " + id;
    }

    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        // Future 相当于是用来存放Executor执行的结果的一种容器
        ArrayList<Future<String>> results = new ArrayList<Future<String>>();
        for (int i = 0; i < 10; i++) {
            results.add(exec.submit(new TaskWithResult(i)));
        }
        for (Future<String> fs : results) {
            if (fs.isDone()) {
                System.out.println("fs.get()" + fs.get());
            } else {
                System.out.println("Future result is not yet complete");
            }
        }
        exec.shutdown();
    }

}
