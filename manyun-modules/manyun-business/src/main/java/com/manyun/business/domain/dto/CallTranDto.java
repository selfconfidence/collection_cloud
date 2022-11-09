package com.manyun.business.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author yanwei
 * @date 2022/7/25
 */
@Data
@Builder
@ToString
public class CallTranDto {
    /**
     *      *     插入艺术品基本信息
     *      *     artId:艺术品Id  自己拥有得藏品得唯一编号  并不是藏品编号
     *      *     date:艺术品年代
     *      *     owner:艺术品拥有者
     */
    private String form;
    private String to;
    private Integer tokenId;

    private String account;
    private String userKey;




}
