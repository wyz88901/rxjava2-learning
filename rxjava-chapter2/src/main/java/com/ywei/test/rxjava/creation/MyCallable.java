package com.ywei.test.rxjava.creation;

import java.util.concurrent.Callable;

/**
 * @author yingzhou.wei
 * @Description
 * @date 2018/8/25
 */
public class MyCallable implements Callable {
    @Override
    public String call() throws Exception {
        System.out.println("模拟 callable 。。。。");
        Thread.sleep(5000L);
        return "OK";
    }
}
