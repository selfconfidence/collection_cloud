package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel("系统余额提现对象返回视图")
@Data
public class SystemWithdrawVo
{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("提现金额")
    private BigDecimal withdrawAmount;

    @ApiModelProperty("剩余余额")
    private BigDecimal moneyBalance;

    @ApiModelProperty("姓名")
    private String userName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("银行卡")
    private String bankCard;

    @ApiModelProperty("是否已打款（0未打款，1已打款）")
    private Integer withdrawStatus;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

}
