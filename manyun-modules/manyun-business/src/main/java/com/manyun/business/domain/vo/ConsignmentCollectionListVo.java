package com.manyun.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.manyun.comm.api.domain.dto.CntUserDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("寄售藏品列表返回视图")
@ToString
public class ConsignmentCollectionListVo implements Serializable {

    @ApiModelProperty("寄售编号")
    private String id;

    @ApiModelProperty("寄售方藏品的中间编号")
    private String buiId;

    @ApiModelProperty("藏品相关视图-按需拿取即可")
    private CollectionVo collectionVo;


    @ApiModelProperty("寄售用户相关-按需拿取即可")
    private CntUserDto cntUserDto;


    @ApiModelProperty("寄售状态相关 1=已寄售(正常),2=已锁单(显示倒计时)")
    private Integer consignmentStatus;

    @ApiModelProperty("剩余支付时间 consignmentStatus = 2 有效  过了这个时间 才可以释放订单,其他人才可以抢购")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endPayTime;


    @ApiModelProperty("寄售时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @ApiModelProperty("寄售服务费")
    private BigDecimal serverCharge;

    @ApiModelProperty("寄售价格")
    private BigDecimal consignmentPrice;

    @ApiModelProperty("藏品编号|寄售专属")
    private String collectionNumber;


}
