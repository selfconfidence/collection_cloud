package com.manyun.admin.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.io.Serializable;

@ApiModel("用户抽签购买藏品或盲盒中间对象")
@TableName("cnt_user_tar")
public class CntUserTar implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("抽签编号")
    private String tarId;

    @ApiModelProperty("开奖时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date openTime;

    @ApiModelProperty("用户编号")
    private String userId;

    @ApiModelProperty("是否购买，1=未购买,2=已购买")
    private Integer goSell;

    @ApiModelProperty("业务编号;(盲盒,藏品的编号)")
    private String buiId;

    @ApiModelProperty("1=已抽中,2=未抽中,4等待开奖")
    private Integer isFull;

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
    public void setOpenTime(Date openTime)
    {
        this.openTime = openTime;
    }

    public Date getOpenTime()
    {
        return openTime;
    }
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserId()
    {
        return userId;
    }
    public void setGoSell(Integer goSell)
    {
        this.goSell = goSell;
    }

    public Integer getGoSell()
    {
        return goSell;
    }
    public void setBuiId(String buiId)
    {
        this.buiId = buiId;
    }

    public String getBuiId()
    {
        return buiId;
    }
    public void setIsFull(Integer isFull)
    {
        this.isFull = isFull;
    }

    public Integer getIsFull()
    {
        return isFull;
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
            .append("openTime", getOpenTime())
            .append("userId", getUserId())
            .append("goSell", getGoSell())
            .append("buiId", getBuiId())
            .append("isFull", getIsFull())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
