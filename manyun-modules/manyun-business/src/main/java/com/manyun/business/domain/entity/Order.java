package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 订单
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@TableName("cnt_order")
@ApiModel(value = "Order对象", description = "订单")
@Data
@ToString
public class Order implements Serializable {

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

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("业务编号")
    private String buiId;





    @ApiModelProperty("用户与业务的唯一编号,没有购买就没有这个编号")
    private String userBuiId;

    @ApiModelProperty("商品类型;0藏品，1盲盒")
    private Integer goodsType;

    @ApiModelProperty("订单状态;0待付款，1已完成，2已取消，-1支付未回调，3=进行中(这个比较特殊 属于寄售的时候用的)")
    private Integer orderStatus;

    @ApiModelProperty("支付方式;0平台，1支付宝，2微信，3银联,4杉德,5连连")
    private Integer payType;

    @ApiModelProperty("购买数量,默认为1")
    private Integer goodsNum;



    @ApiModelProperty("组合支付专属,如果此值不是0.00 就说明使用了余额支付。")
    private BigDecimal moneyBln;

    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    @ApiModelProperty("付款截止时间")
    private LocalDateTime endTime;

    @ApiModelProperty("付款时间")
    private LocalDateTime payTime;

    @ApiModelProperty("藏品名称")
    private String collectionName;

    @ApiModelProperty("寄售限单数量")
    private Integer innerNumber;

    @ApiModelProperty("交易流水号(连连)")
    private String txnSeqno;

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
