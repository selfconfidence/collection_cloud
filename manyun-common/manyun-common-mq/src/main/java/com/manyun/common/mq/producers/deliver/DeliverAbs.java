package com.manyun.common.mq.producers.deliver;

import cn.hutool.extra.spring.SpringUtil;
import com.manyun.common.core.enums.DelayLevelEnum;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.function.Consumer;

/*
 * 统一抽象工厂
 *
 * @author yanwei
 * @create 2022-10-11
 */
public abstract class DeliverAbs {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * @param destination formats: `topicName:tags`
     * @param keys  keys - 用来在rockerMq 控制台 用来单独查询消息
     * @param payLoad    业务消息 - 控制在 2m内，字符串，json 都可
     * @param delayLevelEnum  延时等级 - 必须按照此枚举指定
     * @param timeout  消息发送超时时间 毫秒单位
     */
    protected void asyncSend(String destination, String keys, Object payLoad, DelayLevelEnum delayLevelEnum, long timeout, Consumer<SendResult> successConsumer, Consumer<Throwable> failConsumer){
        Message cancel_order_msg = MessageBuilder.withPayload(payLoad).setHeader(RocketMQHeaders.KEYS,keys).build();
        rocketMQTemplate.asyncSend(destination, cancel_order_msg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                successConsumer.accept(sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                failConsumer.accept(throwable);
            }
        },timeout,delayLevelEnum.getDelay());
    }
}
