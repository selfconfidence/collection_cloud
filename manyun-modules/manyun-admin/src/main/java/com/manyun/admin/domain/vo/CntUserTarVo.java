package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel("用户抽签购买藏品或盲盒中间对象返回视图")
@Data
public class CntUserTarVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("抽签编号")
    private String tarId;

    @ApiModelProperty("开奖时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date openTime;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("是否购买，1=未购买,2=已购买")
    private Integer goSell;

    @ApiModelProperty("业务编号;(盲盒,藏品的编号)")
    private String buiId;

    @ApiModelProperty("1=已抽中,2=未抽中,4等待开奖")
    private Integer isFull;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

}
