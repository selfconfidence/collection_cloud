package com.manyun.common.mq.producers.deliver;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.manyun.common.core.enums.DelayLevelEnum;
import com.manyun.common.mq.config.MqConfig;
import com.manyun.common.mq.producers.msg.DeliverMsg;
import lombok.extern.slf4j.Slf4j;
/*import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;*/
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.function.Function;

/*
 * 延时消息生产工厂
 *
 * @author yanwei
 * @create 2022-10-10
 */
@Component
@Slf4j
public class DeliverProducer extends DeliverAbs {

    @Autowired
    private MqConfig mqConfig;


    /**
     * 抽签开奖方法 延时队列
     * @param keys
     * @param payLoad
     * @param delayLevelEnum
     */
    public void sendTarOpen(String keys, DeliverMsg payLoad, DelayLevelEnum delayLevelEnum){
        String topic = mqConfig.getTopic(0);
        String tar = StrUtil.format("{}:{}", topic, mqConfig.getTarget(topic, 1));
        asyncSend(tar,keys, payLoad,delayLevelEnum,6000,(sendResult)->{
                    log.info("抽签开奖发送消息成功!keys:{},消息内容:{}",keys,payLoad);
                },
                (throwable)->{
                    log.info("抽签开奖发送消息失败!keys:{},消息内容:{},异常信息:{}",keys,payLoad,throwable.getMessage());
                }
        );
    }



    /**
     * 取消普通(寄售)订单发送消息 相关方法!
     * @param keys
     * @param payLoad
     * @param delayLevelEnum
     */
    public void sendCancelOrder(String keys,DeliverMsg payLoad,DelayLevelEnum delayLevelEnum){
        String topic = mqConfig.getTopic(0);
        String tar = StrUtil.format("{}:{}", topic, mqConfig.getTarget(topic, 0));
        asyncSend(tar,keys, payLoad,delayLevelEnum,6000,(sendResult)->{
              log.info("取消普通(寄售)订单发送消息成功!keys:{},消息内容:{}",keys,payLoad);
          },
           (throwable)->{
               log.info("取消普通(寄售)订单发送消息失败!keys:{},消息内容:{},异常信息:{}",keys,payLoad,throwable.getMessage());
         }
         );
    }

    public void sendAuctionStart(String keys, DeliverMsg payLoad, DelayLevelEnum delayLevelEnum) {
        String topic = mqConfig.getTopic(0);
        String tar = StrUtil.format("{}:{}", topic, mqConfig.getTarget(topic, 2));
        asyncSend(tar, keys, payLoad, delayLevelEnum, 6000, (sendResult) -> {
            log.info("拍卖开始发送消息成功!keys:{},消息内容:{}", keys, payLoad);
        },
         (throwable) ->{
            log.info("拍卖开始发送消息失败!keys:{},消息内容:{},异常信息:{}",keys, payLoad, throwable.getMessage());
         }
         );
    }

    public void sendAuctionEnd(String keys, DeliverMsg payLoad, DelayLevelEnum delayLevelEnum) {
        String topic = mqConfig.getTopic(0);
        String tar = StrUtil.format("{}:{}", topic, mqConfig.getTarget(topic, 3));
        asyncSend(tar, keys, payLoad, delayLevelEnum, 6000, (sendResult) -> {
            log.info("拍卖结束发送消息成功!keys:{},消息内容:{}", keys, payLoad);
        },
        (throwable) ->{
            log.info("拍卖结束发送消息失败!keys:{},消息内容:{},异常信息:{}",keys, payLoad, throwable.getMessage());
        }
        );
    }


    public void sendCancelAuctionOrder(String keys, DeliverMsg payLoad, DelayLevelEnum delayLevelEnum) {
        String topic = mqConfig.getTopic(0);
        String tar = StrUtil.format("{}:{}", topic, mqConfig.getTarget(topic, 4));
        asyncSend(tar, keys, payLoad, delayLevelEnum, 6000, (sendResult) -> {
                    log.info("取消拍卖订单发送消息成功!keys:{},消息内容:{}", keys, payLoad);
                },
                (throwable) ->{
                    log.info("取消拍卖订单发送消息失败!keys:{},消息内容:{},异常信息:{}",keys, payLoad, throwable.getMessage());
                }
        );
    }

    public void sendCancelAuctionMargin(String keys, DeliverMsg payLoad, DelayLevelEnum delayLevelEnum) {
        String topic = mqConfig.getTopic(0);
        String tar = StrUtil.format("{}:{}", topic, mqConfig.getTarget(topic, 5));
        asyncSend(tar, keys, payLoad, delayLevelEnum, 6000, (sendResult) -> {
                    log.info("取消保证金发送消息成功!keys:{},消息内容:{}", keys, payLoad);
                },
                (throwable) ->{
                    log.info("取消保证金发送消息失败!keys:{},消息内容:{},异常信息:{}",keys, payLoad, throwable.getMessage());
                }
        );
    }


}
