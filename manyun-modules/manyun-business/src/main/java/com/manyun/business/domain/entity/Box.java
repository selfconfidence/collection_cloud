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
 * 盲盒;盲盒主体表
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@TableName("cnt_box")
@ApiModel(value = "Box对象", description = "盲盒;盲盒主体表")
@Data
@ToString
public class Box implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;


    @ApiModelProperty("盲盒标题")
    private String boxTitle;

    @ApiModelProperty("盲盒现价_实际支付的价格")
    private BigDecimal realPrice;

    @ApiModelProperty("盲盒原价")
    private BigDecimal sourcePrice;



    @ApiModelProperty("分类编号")
    private String cateId;

    @ApiModelProperty("已售")
    private Integer selfBalance;

    @ApiModelProperty("库存")
    private Integer balance;

    @ApiModelProperty("盲盒状态;0=下架,1=正常,2=售罄")
    private Integer statusBy;

    @ApiModelProperty("盲盒详情")
    private String boxInfo;

    @ApiModelProperty("发售时间;到达对应时间点才可以正常交易_平台")
    private LocalDateTime publishTime;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedTime;

}
