package com.manyun.business.domain.dto;
import lombok.Builder;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
/**
 * 订单服务内创建订单基本信息
 */
@Builder
public class OrderCreateDto implements Serializable {

    private Integer payType;

    private Integer goodsNum;

    private String userId;

    private String buiId;

    private Integer goodsType;


    private BigDecimal orderAmount;


    private String collectionName;

}
