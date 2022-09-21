package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ApiModel("转赠记录对象返回视图")
@Data
public class CntPassonRecordVo
{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("转出用户id")
    private String oldUserId;

    @ApiModelProperty("转出用户昵称")
    private String oldNickName;

    @ApiModelProperty("转出用户手机号")
    private String oldUserPhone;

    @ApiModelProperty("转入用户id")
    private String newUserId;

    @ApiModelProperty("转入用户昵称")
    private String newNickName;

    @ApiModelProperty("转入用户手机号")
    private String newUserPhone;

    @ApiModelProperty("商品id")
    private String goodsId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品缩略图")
    private List<MediaVo> thumbnailImgMediaVos;

    @ApiModelProperty("价格")
    private BigDecimal price;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

}
