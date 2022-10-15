package com.manyun.business.mq.consumers.deliver;

import com.manyun.business.mq.consumers.CommonConsumer;
import com.manyun.business.service.IOrderService;
import com.manyun.common.mq.producers.msg.DeliverMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.PushConsumer*/
;

/*
 * 延时消息监听组
 * 取消订单的延时队列
 * @author yanwei
 * @create 2022-10-10
 */
@Component
@Slf4j
@RocketMQMessageListener(consumeMode = ConsumeMode.CONCURRENTLY,consumerGroup = "delivers_consumer_cancelOrder_group",selectorExpression = "cancelOrder",topic = "delivers_bui")
public class CancelOrderConsumer extends CommonConsumer implements RocketMQListener<DeliverMsg> {

    @Autowired
    private IOrderService orderService;

    @Override
    public void onMessage(DeliverMsg message){
        uniCheck(message.getResetHost(), ()->{
            log.info("取消订单消息消费成功,消息内容为:{}",message);
            orderService.cancelOrder(message.getBuiId());
        }, (throwable)->{
            log.error("取消订单消息消费失败,消息内容:{},错误原因:{}",message,throwable.getMessage());
        });
    }

}
