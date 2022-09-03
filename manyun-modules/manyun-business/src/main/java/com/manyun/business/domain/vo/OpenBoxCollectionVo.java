package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/*
 * 开启盲盒藏品返回视图
 *
 * @author yanwei
 * @create 2022-09-03
 */
@Data
@ApiModel("我的送拍返回视图")
@Builder
public class OpenBoxCollectionVo implements Serializable {


    @ApiModelProperty("藏品编号")
    private String id;

    @ApiModelProperty("藏品名称")
    private String collectionName;

    @ApiModelProperty("藏品主图")
    private List<MediaVo> mediaVos;

    @ApiModelProperty("返回信息")
    private String info;


}
