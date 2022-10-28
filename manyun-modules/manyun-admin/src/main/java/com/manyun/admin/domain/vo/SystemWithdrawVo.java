package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.manyun.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel("系统余额提现对象返回视图")
@Data
public class SystemWithdrawVo
{

    @Excel(name = "主键",width = 30)
    @ApiModelProperty("主键")
    private String id;

    @Excel(name = "提现单号",width = 30)
    @ApiModelProperty("提现单号")
    private String orderNo;

    @Excel(name = "姓名")
    @ApiModelProperty("姓名")
    private String userName;

    @Excel(name = "手机号")
    @ApiModelProperty("手机号")
    private String phone;

    @Excel(name = "银行卡",width = 30)
    @ApiModelProperty("银行卡")
    private String bankCard;

    @Excel(name = "支付宝账户")
    @ApiModelProperty("支付宝账户")
    private String aliAccount;

    @Excel(name = "提现详情",width = 50)
    @ApiModelProperty("提现详情")
    private String withdrawMsg;

    @Excel(name = "提现金额")
    @ApiModelProperty("提现金额")
    private BigDecimal withdrawAmount;

    @Excel(name = "实际到账金额")
    @ApiModelProperty("实际到账金额")
    private BigDecimal realWithdrawAmount;

    @Excel(name = "剩余余额")
    @ApiModelProperty("剩余余额")
    private BigDecimal moneyBalance;

    @Excel(name = "打款状况",readConverterExp = "0=未打款,1=已打款",combo = "未打款,已打款")
    @ApiModelProperty("是否已打款（0未打款，1已打款）")
    private Integer withdrawStatus;

    @Excel(name = "创建时间",width = 30,dateFormat = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @Excel(name = "更新时间",width = 30,dateFormat = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

}
