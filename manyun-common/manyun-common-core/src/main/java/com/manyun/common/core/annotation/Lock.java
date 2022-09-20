package com.manyun.common.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Lock {
    /**
     * lock key
     */
    String value();

    /**
     * 锁超时时间，默认5000ms
     */
    long expireTime() default 5000L;

    /**
     * 锁等待时间，默认50ms
     */
    long waitTime() default 50L;

}
