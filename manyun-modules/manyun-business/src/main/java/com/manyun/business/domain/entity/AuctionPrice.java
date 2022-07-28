package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 竞价表
 * </p>
 *
 * @author 
 * @since 2022-06-30
 */
@TableName("cnt_auction_price")
@ApiModel(value = "AuctionPrice对象", description = "竞价表")
@Data
public class AuctionPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("送拍id")
    private String auctionSendId;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("出价")
    private BigDecimal bidPrice;

    @ApiModelProperty("竞拍状态")
    private Integer auctionStatus;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedTime;

    @ApiModelProperty("是否最新：1最新，2旧")
    private Integer isNew;
}
