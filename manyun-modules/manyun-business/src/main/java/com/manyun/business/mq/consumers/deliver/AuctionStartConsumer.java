package com.manyun.business.mq.consumers.deliver;

import com.manyun.business.mq.consumers.CommonConsumer;
import com.manyun.business.service.IAuctionSendService;
import com.manyun.common.mq.producers.msg.DeliverMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RocketMQMessageListener(consumeMode = ConsumeMode.CONCURRENTLY,consumerGroup = "delivers_consumer_auctionStart_group",selectorExpression = "auctionStart",topic = "delivers_bui")
public class AuctionStartConsumer extends CommonConsumer implements RocketMQListener<DeliverMsg> {

    @Autowired
    private IAuctionSendService auctionSendService;

    @Override
    public void onMessage(DeliverMsg message) {
        uniCheck(message.getResetHost(), ()->{
            log.info("拍卖开始消息消费成功,消息内容为:{}",message);
            auctionSendService.startAuction(message.getBuiId());
        }, (throwable)->{
            log.error("拍卖开始消息消费失败,消息内容:{},错误原因:{}",message,throwable.getMessage());
        });
    }
}
