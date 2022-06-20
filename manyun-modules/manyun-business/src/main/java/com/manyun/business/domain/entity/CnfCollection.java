package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 藏品表
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@TableName("cnt_collection")
@ApiModel(value = "Collection对象", description = "藏品表")
@Data
@ToString
public class CnfCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;


    @ApiModelProperty("系列编号")
    private String cateId;

    @ApiModelProperty("藏品名称")
    private String collectionName;

    @ApiModelProperty("藏品原价")
    private BigDecimal sourcePrice;

    @ApiModelProperty("藏品现价_实际支付的价格")
    private BigDecimal realPrice;

    @ApiModelProperty("藏品编号;购买后")
    private String collectionNumber;

    @ApiModelProperty("藏品哈希;购买后")
    private String collectionHash;

    @ApiModelProperty("链上地址;购买后")
    private String linkAddr;

    @ApiModelProperty("认证机构;购买后")
    private String realCompany;

    @ApiModelProperty("是否上链;1=未上链,2=已上链")
    private Integer isLink;

    @ApiModelProperty("已售")
    private Integer selfBalance;

    @ApiModelProperty("库存")
    private Integer balance;

    @ApiModelProperty("版本标识")
    private Integer versionFlag;

    @ApiModelProperty("藏品状态;0=下架,1=正常,2=售罄")
    private Integer statusBy;

    @ApiModelProperty("发售时间;到达对应时间点才可以正常交易_平台")
    private LocalDateTime publishTime;

    @ApiModelProperty("创作者编号;当前系列的创作者编号")
    private String bindCreation;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;


    public void createD(String createId){
        this.createdBy = createId;
        this.createdTime = LocalDateTime.now();
        if (this.createdTime != null)
            this.updatedTime = this.createdTime;
        this.updatedBy = this.createdBy;
    }

    public void updateD(String updateId){
        this.updatedBy = updateId;
        this.updatedTime = LocalDateTime.now();
    }

}
