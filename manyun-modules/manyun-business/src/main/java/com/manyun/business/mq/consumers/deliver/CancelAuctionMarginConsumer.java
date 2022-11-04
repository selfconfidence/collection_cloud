package com.manyun.business.mq.consumers.deliver;

import com.manyun.business.mq.consumers.CommonConsumer;
import com.manyun.business.service.IAuctionPriceService;
import com.manyun.common.mq.producers.msg.DeliverMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RocketMQMessageListener(consumeMode = ConsumeMode.CONCURRENTLY,consumerGroup = "deliver_consumer_cancelAuctionMargin_group",selectorExpression = "cancelAuctionMargin", topic = "delivers_bui")
public class CancelAuctionMarginConsumer extends CommonConsumer implements RocketMQListener<DeliverMsg> {

    @Autowired
    private IAuctionPriceService auctionPriceService;

    @Override
    public void onMessage(DeliverMsg message) {
        uniCheck(message.getResetHost(), ()->{
            log.info("取消保证金消息消费成功,消息内容为:{}",message);
            //auctionSendService.startAuction(message.getBuiId());
            auctionPriceService.cancelAuctionMargin(message.getBuiId());
        }, (throwable)->{
            log.error("取消保证金消息消费失败,消息内容:{},错误原因:{}",message,throwable.getMessage());
        });
    }
}
