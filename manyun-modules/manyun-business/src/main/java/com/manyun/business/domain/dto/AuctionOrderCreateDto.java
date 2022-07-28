package com.manyun.business.domain.dto;


import lombok.Builder;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
/**
 * 拍卖创建订单基本信息
 */
@Builder
public class AuctionOrderCreateDto implements Serializable {

    private Integer payType;

    private String fromUserId;

    private String toUserId;

    private String goodsId;

    private Integer goodsType;

    private String goodsName;

    private String goodsImg;

    private String sendAuctionId;

    private String auctionPriceId;

    private BigDecimal nowPrice;


}
