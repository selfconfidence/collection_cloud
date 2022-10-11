package com.manyun.business.mq.consumers.deliver;

import cn.hutool.core.thread.ThreadUtil;
import com.manyun.business.mapper.ResetMapper;
import com.manyun.business.mq.consumers.CommonConsumer;
import com.manyun.common.mq.producers.msg.DeliverMsg;
import lombok.extern.slf4j.Slf4j;
/*import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.PushConsumer*/;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Collections;

/*
 * 延时消息监听组
 *  抽签开奖的延时队列
 * @author yanwei
 * @create 2022-10-10
 */
@Component
@Slf4j
@RocketMQMessageListener(consumeMode = ConsumeMode.CONCURRENTLY,consumerGroup = "delivers_consumer_tarOpen_group",selectorExpression = "tarOpen",topic = "delivers_bui")
public class TarOpenConsumer extends CommonConsumer implements RocketMQListener<DeliverMsg> {



    @Override
    public void onMessage(DeliverMsg message){
        uniCheck(message.getResetHost(), ()->{
            log.info("抽签开奖消息消费成功");
        }, (throwable)->{
            log.error("抽签开奖消息消费失败,错误原因:{}",throwable.getMessage());
        });
    }



}
