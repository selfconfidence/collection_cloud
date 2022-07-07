package com.manyun.business.design.delay;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *  单环境执行,分布式环境下失效！！！
 */
public  abstract class DelayAbsAspect<K> implements DelayInvocation<K>{

    private Object lock = new Object();

    protected volatile Thread thisThread = Thread.currentThread();

    // 默认执行策略 - 不执行
    //private volatile boolean isExc = Boolean.FALSE;

    /**
     * 加入 某延迟执行
     *
     */
    public void asyncInvoker(long time, K k, Consumer<K> consumer){
        synchronized (lock){
                thisThread = Thread.currentThread();
                try {
                    lock.wait(time);
                    if (Thread.State.RUNNABLE.equals(thisThread.getState()))
                    // 执行此步,代表回调成功!
                    invocationSuccess(k);
                    // 删除对应  key 防止内存泄露！！！
                    consumer.accept(k);
                }catch (InterruptedException interruptedException){

                }

        }
    }


}
