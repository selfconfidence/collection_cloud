package com.manyun.common.mq.producers.msg;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/*
 * 消息实体
 *
 * @author yanwei
 * @create 2022-10-11
 */
@Data
@ToString
public class DeliverMsg implements Serializable {

    /**
     * 业务编号
     */
    private String buiId;

    /**
     * 业务名称
     */
    private String buiName;

    /**
     * resetHost
     */
    private String resetHost;
}
