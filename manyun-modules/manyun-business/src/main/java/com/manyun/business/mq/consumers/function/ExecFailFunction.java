package com.manyun.business.mq.consumers.function;


@FunctionalInterface
public interface ExecFailFunction {
     void fail(Throwable t);
}
