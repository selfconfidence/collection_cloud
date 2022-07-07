package com.manyun.business.design.delay;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 *
 * 延时执行抽象
 */
public interface DelayInvocation<K> {

   void invocationSuccess(K k);

   void invocationFail(K k);

   void asyncInvoker(long time,K k,Consumer<K> consumer);

}
