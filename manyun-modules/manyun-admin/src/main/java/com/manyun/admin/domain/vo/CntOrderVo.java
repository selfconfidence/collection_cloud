package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ApiModel("订单返回视图")
@Data
public class CntOrderVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("用户与业务的唯一编号,没有购买就没有这个编号")
    private String userBuiId;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户电话号")
    private String phone;

    @ApiModelProperty("商品类型;0藏品，1盲盒")
    private Integer goodsType;

    @ApiModelProperty("商品id")
    private String buiId;

    @ApiModelProperty("商品名称")
    private String collectionName;

    @ApiModelProperty("商品缩略图")
    private List<MediaVo> thumbnailImgMediaVos;

    @ApiModelProperty("购买数量")
    private Integer goodsNum;

    @ApiModelProperty("藏品hash")
    private String collectionHash;

    @ApiModelProperty("订单状态;0待付款，1已完成，2已取消，-1支付未回调 3=进行中(这个比较特殊 属于寄售的时候用的)")
    private Integer orderStatus;

    @ApiModelProperty("支付方式;0平台，1支付宝，2微信，3银联")
    private Integer payType;

    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("付款时间")
    private Date payTime;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createdTime;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private Date updatedTime;

}
