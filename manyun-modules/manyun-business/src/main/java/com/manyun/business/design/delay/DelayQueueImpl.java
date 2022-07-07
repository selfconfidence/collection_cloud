package com.manyun.business.design.delay;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class DelayQueueImpl<K extends Serializable> implements DelayQueue<K> , InitializingBean {

    private ConcurrentHashMap<K, DelayHandler> businessMap = null;



    /**
     * 唯一 k 指定
     *
     * @param k  必须唯一，在同一个资源锁定标识中做 延迟操作
     * @param time 1 = 秒单位
     */
    @Override
    @Async
    public void put(K k, long time,DelayInvocation delayInvocationCallBack) {
        businessMap.merge(k, new DelayHandler(delayInvocationCallBack), (oldValue, newValue) -> {
            // 立即失败！！！
            oldValue.invocationFail(k);
            return newValue;
        }).asyncInvoker(TimeUnit.SECONDS.toMillis(time),k,(keyFlag)->clearM((K) keyFlag));
    }

    @Override
    public void clearM(K k) {
        businessMap.remove(k);
    }

    @Override
    public void clearAll() {
        businessMap.clear();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化固定方法
        businessMap = new ConcurrentHashMap<>(16);
    }

    static class DelayHandler<K> extends DelayAbsAspect<K>{

        private DelayInvocation delayInvocation;

        public DelayHandler(DelayInvocation delayInvocation) {
            this.delayInvocation = delayInvocation;
        }

        @Override
        public void invocationSuccess(K k) {
            delayInvocation.invocationSuccess(k);
        }

        @Override
        public void invocationFail(K k) {
            //if (Objects.nonNull(thisThread))
          thisThread.interrupt();
          delayInvocation.invocationFail(k);
        }

    }



}
