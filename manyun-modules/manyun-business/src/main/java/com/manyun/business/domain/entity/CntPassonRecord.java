package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@ApiModel("转赠记录对象")
@TableName("cnt_passon_record")
@Data
@ToString
public class CntPassonRecord implements Serializable
{
    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty("图片id")
    private String pictureId;

    @ApiModelProperty("图片类型")
    private String pictureType;

    @ApiModelProperty("价格")
    private BigDecimal price;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    public void createD(String createId){
        this.createdBy = createId;
        this.createdTime = LocalDateTime.now();
        if (this.createdTime != null)
            this.updatedTime = this.createdTime;
        this.updatedBy = this.createdBy;
    }

    public void updateD(String updateId){
        this.updatedBy = updateId;
        this.updatedTime = LocalDateTime.now();
    }

}
