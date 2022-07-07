package com.manyun.business.design.delay;

import org.springframework.scheduling.annotation.Async;

import java.io.Serializable;

public interface DelayQueue<K extends Serializable> {




    void put(K k, long time, DelayInvocation delayInvocationCallBack);

    void clearM(K m);

    void clearAll();
}
