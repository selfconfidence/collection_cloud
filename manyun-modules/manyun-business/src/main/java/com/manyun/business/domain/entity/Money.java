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
 * 钱包表
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@TableName("cnt_money")
@ApiModel(value = "Money对象", description = "钱包表")
@Data
@ToString
public class Money implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("用户编号")
    private String userId;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("真实手机号")
    private String realPhone;

    @ApiModelProperty("身份证号")
    private String cartNo;

    @ApiModelProperty("身份证正面_图片链接")
    private String cartJust;

    @ApiModelProperty("身份证反面_图片链接")
    private String cartBack;

    @ApiModelProperty("钱包余量")
    private BigDecimal moneyBalance;

    @ApiModelProperty("是否绑定银行卡  1=绑定,2=未绑定")
    private Integer isBindCart;

    @ApiModelProperty("银行卡名称;中国银行,工商银行等")
    private String bankName;

    @ApiModelProperty("银行卡号")
    private String bankCart;

    @ApiModelProperty("银行开户行")
    private String bankOpen;

    @ApiModelProperty("连连开户状态")
    private String llAccountStatus;

    @ApiModelProperty("杉德钱包开户状态: 0未开户，1已开户")
    private Integer sandAccountStatus;

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
