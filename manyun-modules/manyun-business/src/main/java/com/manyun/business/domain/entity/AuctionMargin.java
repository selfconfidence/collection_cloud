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
 * 保证金表
 * </p>
 *
 * @author 
 * @since 2022-07-28
 */
@TableName("cnt_auction_margin")
@ApiModel(value = "AuctionMargin对象", description = "保证金表")
@Data
public class AuctionMargin implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedTime;

    @ApiModelProperty("送拍id")
    private String auctionSendId;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("保证金")
    private BigDecimal margin;

    @ApiModelProperty("组合支付专属,如果此值不是0.00 就说明使用了余额支付。")
    private BigDecimal moneyBln;

    @ApiModelProperty("支付保证金状态，0：失败，1，成功")
    private Integer payMarginStatus;

    @ApiModelProperty("付款截止时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "支付类型,1=微信,2=支付宝,0=余额支付，3=银联, 4= 杉德")
    private Integer payType;

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
