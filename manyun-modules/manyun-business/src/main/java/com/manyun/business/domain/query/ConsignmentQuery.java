package com.manyun.business.domain.query;

import com.manyun.common.core.domain.RequestTimeRate;
import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Data
@ApiModel("分页查询寄售市场资产相关查询视图")
public class ConsignmentQuery extends RequestTimeRate {
/*
    @ApiModelProperty("是否最新 0=默认,1=最新")
    private Integer isNew = 0;*/

    @ApiModelProperty("价格排序查询 0=倒序,1=正序(默认倒序) -1 代表不用此字段排序")
    private Integer priceOrder = 0;

    @ApiModelProperty("时间排序查询 0=倒序,1=正序(默认倒序) -1 代表不用此字段排序")
    private Integer timeOrder = 0;

    @ApiModelProperty("搜索标题")
    private String commName;

    @ApiModelProperty("系列编号")
    private String cateId;


    @ApiModelProperty(value = "起始页码",required = true)
    private  Integer pageNum;

    @ApiModelProperty(value = "查询每页多少条",required = true)
    @Max(value = 10L,message = "页码有误！")
    private  Integer pageSize;

   // @NotBlank(message = "验证Token不可为空。")
    //private String token;



}
