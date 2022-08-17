package com.manyun.business.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AsyncUtil {
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * 提交一个需要异步执行的任务
     */
    public static Future<?> submit(Runnable task) {
        return executorService.submit(task);
    }
}
