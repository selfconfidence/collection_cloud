package com.manyun.admin.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.io.Serializable;

@ApiModel("提前购配置-只能有一条对象")
@TableName("cnt_post_config")
public class CntPostConfig implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("配置名称")
    private String configName;

    @ApiModelProperty("备注")
    private String reMark;

    @ApiModelProperty("可购买次数")
    private Integer buyFrequency;

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

    @ApiModelProperty("(藏品|盲盒)编号(提前购验证模板)")
    private String buiId;

    @ApiModelProperty("0=藏品,1=盲盒")
    private Integer isType;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setConfigName(String configName)
    {
        this.configName = configName;
    }

    public String getConfigName()
    {
        return configName;
    }
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    public String getReMark() {
        return reMark;
    }

    public void setReMark(String reMark) {
        this.reMark = reMark;
    }

    public Integer getBuyFrequency() {
        return buyFrequency;
    }

    public void setBuyFrequency(Integer buyFrequency) {
        this.buyFrequency = buyFrequency;
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
    public void setBuiId(String buiId)
    {
        this.buiId = buiId;
    }

    public String getBuiId()
    {
        return buiId;
    }
    public void setIsType(Integer isType)
    {
        this.isType = isType;
    }

    public Integer getIsType()
    {
        return isType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("configName", getConfigName())
            .append("reMark", getReMark())
            .append("buyFrequency", getBuyFrequency())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .append("buiId", getBuiId())
            .append("isType", getIsType())
            .toString();
    }
}
