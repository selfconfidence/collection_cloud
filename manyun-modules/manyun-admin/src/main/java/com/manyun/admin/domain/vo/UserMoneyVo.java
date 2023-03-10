package com.manyun.admin.domain.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
@ApiModel("用户管理列表返回视图")
public class UserMoneyVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("帐号状态（0正常 1停用）")
    private String status;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("联系方式")
    private String phone;

    @ApiModelProperty("用户头像")
    private String headImage;

    @ApiModelProperty("邀请码")
    private String pleaseCode;

    @ApiModelProperty("区块链地址")
    private String linkAddr;

    @ApiModelProperty("父编码")
    private String parentId;

    @ApiModelProperty("邀请总人数")
    private Long inviteNumber;

    @ApiModelProperty("已实名人数")
    private Long realNumber;

    @ApiModelProperty("购买过某商品人数")
    private Long inviteGoodsNumber;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("卡号")
    private String bankCart;

    @ApiModelProperty("钱包余量")
    private BigDecimal moneyBalance;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createdTime;

}
