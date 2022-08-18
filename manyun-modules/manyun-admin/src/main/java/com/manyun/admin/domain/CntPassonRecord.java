package com.manyun.admin.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.io.Serializable;

@ApiModel("转赠记录对象")
@TableName("cnt_passon_record")
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
    private Date createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setOldUserId(String oldUserId)
    {
        this.oldUserId = oldUserId;
    }

    public String getOldUserId()
    {
        return oldUserId;
    }
    public void setOldNickName(String oldNickName)
    {
        this.oldNickName = oldNickName;
    }

    public String getOldNickName()
    {
        return oldNickName;
    }
    public void setOldUserPhone(String oldUserPhone)
    {
        this.oldUserPhone = oldUserPhone;
    }

    public String getOldUserPhone()
    {
        return oldUserPhone;
    }
    public void setNewUserId(String newUserId)
    {
        this.newUserId = newUserId;
    }

    public String getNewUserId()
    {
        return newUserId;
    }
    public void setNewNickName(String newNickName)
    {
        this.newNickName = newNickName;
    }

    public String getNewNickName()
    {
        return newNickName;
    }
    public void setNewUserPhone(String newUserPhone)
    {
        this.newUserPhone = newUserPhone;
    }

    public String getNewUserPhone()
    {
        return newUserPhone;
    }
    public void setGoodsId(String goodsId)
    {
        this.goodsId = goodsId;
    }

    public String getGoodsId()
    {
        return goodsId;
    }
    public void setGoodsName(String goodsName)
    {
        this.goodsName = goodsName;
    }

    public String getGoodsName()
    {
        return goodsName;
    }
    public void setPictureId(String pictureId)
    {
        this.pictureId = pictureId;
    }

    public String getPictureId()
    {
        return pictureId;
    }
    public void setPictureType(String pictureType)
    {
        this.pictureType = pictureType;
    }

    public String getPictureType()
    {
        return pictureType;
    }
    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public BigDecimal getPrice()
    {
        return price;
    }
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }
    public void setCreatedTime(Date createdTime)
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime()
    {
        return createdTime;
    }
    public void setUpdatedBy(String updatedBy)
    {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedBy()
    {
        return updatedBy;
    }
    public void setUpdatedTime(Date updatedTime)
    {
        this.updatedTime = updatedTime;
    }

    public Date getUpdatedTime()
    {
        return updatedTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("oldUserId", getOldUserId())
            .append("oldNickName", getOldNickName())
            .append("oldUserPhone", getOldUserPhone())
            .append("newUserId", getNewUserId())
            .append("newNickName", getNewNickName())
            .append("newUserPhone", getNewUserPhone())
            .append("goodsId", getGoodsId())
            .append("goodsName", getGoodsName())
            .append("pictureId", getPictureId())
            .append("pictureType", getPictureType())
            .append("price", getPrice())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
