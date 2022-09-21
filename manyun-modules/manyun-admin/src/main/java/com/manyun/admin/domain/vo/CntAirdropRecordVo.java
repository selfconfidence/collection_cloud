package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@ApiModel("空投记录对象返回视图")
@Data
public class CntAirdropRecordVo
{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户手机号")
    private String userPhone;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品缩略图")
    private List<MediaVo> thumbnailImgMediaVos;

    @ApiModelProperty("商品类型")
    private Integer goodsType;

    @ApiModelProperty("投递类型 0:空投 1:批量空投")
    private Integer deliveryType;

    @ApiModelProperty("投递状态 0:投递成功 1:投递失败")
    private Integer deliveryStatus;

    @ApiModelProperty("投递详情")
    private String deliveryInfo;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

}
