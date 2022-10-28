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
 * 系统余额提现
 * </p>
 *
 * @author yanwei
 * @since 2022-09-16
 */
@TableName("cnt_system_withdraw")
@Data
@ApiModel(value = "CntSystemWithdraw对象", description = "系统余额提现")
public class CntSystemWithdraw implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("提现金额")
    private BigDecimal withdrawAmount;

    @ApiModelProperty("实际到账金额")
    private BigDecimal realWithdrawAmount;

    @ApiModelProperty("剩余余额")
    private BigDecimal moneyBalance;

    @ApiModelProperty("姓名")
    private String userName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("银行卡")
    private String bankCard;

    @ApiModelProperty("支付宝账户")
    private String aliAccount;

    @ApiModelProperty("提现详情")
    private String withdrawMsg;

    @ApiModelProperty("是否已打款（0未打款，1已打款）")
    private Integer withdrawStatus;

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
