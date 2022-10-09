package com.manyun.admin.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.io.Serializable;

@ApiModel("抽签记录日志对象")
@TableName("cnt_user_tar_log")
public class CntUserTarLog implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("抽签编号")
    private String tarId;

    @ApiModelProperty("用户手机号")
    private String userPhone;

    @ApiModelProperty("商品编号")
    private String goodsId;

    @ApiModelProperty("投递类型 0:藏品 1:盲盒")
    private Integer goodsType;

    @ApiModelProperty("1=已抽中,2=未抽中,4等待开奖")
    private Integer isFull;

    @ApiModelProperty("状态 0:成功 1:失败")
    private Integer status;

    @ApiModelProperty("详情")
    private String info;

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
    public void setTarId(String tarId)
    {
        this.tarId = tarId;
    }

    public String getTarId()
    {
        return tarId;
    }
    public void setUserPhone(String userPhone)
    {
        this.userPhone = userPhone;
    }

    public String getUserPhone()
    {
        return userPhone;
    }
    public void setGoodsId(String goodsId)
    {
        this.goodsId = goodsId;
    }

    public String getGoodsId()
    {
        return goodsId;
    }
    public void setGoodsType(Integer goodsType)
    {
        this.goodsType = goodsType;
    }

    public Integer getGoodsType()
    {
        return goodsType;
    }
    public void setIsFull(Integer isFull)
    {
        this.isFull = isFull;
    }

    public Integer getIsFull()
    {
        return isFull;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }
    public void setInfo(String info)
    {
        this.info = info;
    }

    public String getInfo()
    {
        return info;
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
            .append("tarId", getTarId())
            .append("userPhone", getUserPhone())
            .append("goodsId", getGoodsId())
            .append("goodsType", getGoodsType())
            .append("isFull", getIsFull())
            .append("status", getStatus())
            .append("info", getInfo())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
