package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel("盲盒改动接收参数视图")
@Data
public class CntBoxAlterVo {

    @ApiModelProperty("主键 增加时删除该参数不需要 修改时传入该参数")
    private String id;

    @ApiModelProperty("分类编号")
    private String cateId;

    @ApiModelProperty("可以提前购的盲盒子 - 如果为null 就代表不是提前购")
    private Integer postTime;

    @ApiModelProperty("抽签编号 如果为null不需要抽签,否则需要抽签")
    private String tarId;

    @ApiModelProperty("盲盒标题")
    private String boxTitle;

    @ApiModelProperty("已售")
    private Integer selfBalance;

    @ApiModelProperty("库存")
    private Integer balance;

    @ApiModelProperty("盲盒状态;0=下架,1=正常,2=售罄 ")
    private Integer statusBy;

    @ApiModelProperty("盲盒详情")
    private String boxInfo;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("发售时间")
    private Date publishTime;

    @ApiModelProperty("藏品原价")
    private BigDecimal sourcePrice;

    @ApiModelProperty("藏品实际支付的价格")
    private BigDecimal realPrice;

}
