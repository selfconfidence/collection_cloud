package com.manyun.admin.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.io.Serializable;

@ApiModel("用户购买盲盒中间对象")
@TableName("cnt_user_box")
public class CntUserBox implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("盲盒标题")
    private String boxTitle;

    @ApiModelProperty("1未开启,2已开启")
    private Long boxOpen;

    @ApiModelProperty("用户编号")
    private String userId;

    @ApiModelProperty("盲盒编号")
    private String boxId;

    @ApiModelProperty("1=存在,2=不存在(寄售回滚有用)")
    private Long isExist;

    @ApiModelProperty("来源")
    private String sourceInfo;

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

    public String getBoxTitle() {
        return boxTitle;
    }

    public void setBoxTitle(String boxTitle) {
        this.boxTitle = boxTitle;
    }

    public void setBoxOpen(Long boxOpen)
    {
        this.boxOpen = boxOpen;
    }

    public Long getBoxOpen()
    {
        return boxOpen;
    }
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserId()
    {
        return userId;
    }
    public void setBoxId(String boxId)
    {
        this.boxId = boxId;
    }

    public String getBoxId()
    {
        return boxId;
    }
    public void setIsExist(Long isExist)
    {
        this.isExist = isExist;
    }

    public Long getIsExist()
    {
        return isExist;
    }
    public void setSourceInfo(String sourceInfo)
    {
        this.sourceInfo = sourceInfo;
    }

    public String getSourceInfo()
    {
        return sourceInfo;
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
            .append("boxOpen", getBoxOpen())
            .append("userId", getUserId())
            .append("boxId", getBoxId())
            .append("isExist", getIsExist())
            .append("sourceInfo", getSourceInfo())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
