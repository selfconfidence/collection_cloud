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
 * 寄售市场主表
 * </p>
 *
 * @author yanwei
 * @since 2022-06-30
 */
@TableName("cnt_consignment")
@ApiModel(value = "CntConsignment对象", description = "寄售市场主表")
@Data
@ToString
public class CntConsignment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("(盲盒|藏品) 中间表编号")
    private String buiId;

    @ApiModelProperty("系列编号 | 分类编号")
    private String cateId;

    @ApiModelProperty("(盲盒|藏品) 名称")
    private String buiName;


    @ApiModelProperty("(盲盒|藏品) 编号")
    private String realBuiId;

    @ApiModelProperty("关联的订单编号（（consignment_status =2 有效）")
    private String orderId;

    @ApiModelProperty("0藏品，1盲盒")
    private Integer isType;

    @ApiModelProperty("手续费")
    private BigDecimal serverCharge;

    @ApiModelProperty("寄售价格")
    private BigDecimal consignmentPrice;

    @ApiModelProperty("寄售用户编号")
    private String sendUserId;

    @ApiModelProperty("购买人的用户编号")
    private String payUserId;

    @ApiModelProperty("0待打款,1=已打款（consignment_status = 3此字段有效）		用来让后台用户审核打款用的")
    private Integer toPay;

    @ApiModelProperty("寄售状态- 1=已寄售,2=已锁单(有买方,未支付而已)  3=已售出")
    private Integer consignmentStatus;

    @ApiModelProperty("词条")
    private String formInfo;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
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
