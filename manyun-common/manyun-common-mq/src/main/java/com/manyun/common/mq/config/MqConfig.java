package com.manyun.common.mq.config;

import cn.hutool.core.lang.Assert;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
/*import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientConfigurationBuilder;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;*/
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/*
 * 初始化配置
 *
 * @author yanwei
 * @create 2022-10-10
 */
@ConfigurationProperties(prefix = "mqconfig")
@Slf4j
@Data
public class MqConfig {

    private List<Delivers> delivers;

    public void setDelivers(List<Delivers> delivers) {
        this.delivers = delivers;
    }

    public String getTopic(int index){
        checkPost(index);
        return delivers.get(index).getTopic();
    }

    public String getTarget(String topic,int index){
        checkPost(index);
        for (Delivers deliver : this.delivers) {
            if (deliver.getTopic().equals(topic)){
               return deliver.getTars().get(index).getTarValue();
            }
        }
        throw new IllegalStateException("index is not found tars ！");
    }

    private void checkPost(Integer index){
        Assert.isTrue(!delivers.isEmpty(),"delivers.size == 0");
        Assert.isTrue(index >=0,"index range -1 ?");
    }
     @Data
     static class Delivers{
      private String topic;
      private List<Tars> tars;

    }

    @Data
     static class Tars{
        private String tarName;
        private String tarValue;

    }
}


