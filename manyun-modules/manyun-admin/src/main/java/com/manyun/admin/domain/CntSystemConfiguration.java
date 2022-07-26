package com.manyun.admin.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@ApiModel("系统配置对象")
@TableName("cnt_system_configuration")
public class CntSystemConfiguration implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("项目名称")
    private String name;

    @ApiModelProperty("logo")
    private String logo;

    @ApiModelProperty("公司邮箱")
    private String companyMail;

    @ApiModelProperty("客服电话")
    private String telPhone;

    @ApiModelProperty("客服邮箱")
    private String serviceMail;

    @ApiModelProperty("商务合作文案")
    private String remind;

    @ApiModelProperty("工作时间")
    private String workingHours;

    @ApiModelProperty("工作qq群")
    private String shopQun;

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
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setLogo(String logo)
    {
        this.logo = logo;
    }

    public String getLogo()
    {
        return logo;
    }
    public void setCompanyMail(String companyMail)
    {
        this.companyMail = companyMail;
    }

    public String getCompanyMail()
    {
        return companyMail;
    }
    public void setTelPhone(String telPhone)
    {
        this.telPhone = telPhone;
    }

    public String getTelPhone()
    {
        return telPhone;
    }
    public void setServiceMail(String serviceMail)
    {
        this.serviceMail = serviceMail;
    }

    public String getServiceMail()
    {
        return serviceMail;
    }
    public void setRemind(String remind)
    {
        this.remind = remind;
    }

    public String getRemind()
    {
        return remind;
    }
    public void setWorkingHours(String workingHours)
    {
        this.workingHours = workingHours;
    }

    public String getWorkingHours()
    {
        return workingHours;
    }
    public void setShopQun(String shopQun)
    {
        this.shopQun = shopQun;
    }

    public String getShopQun()
    {
        return shopQun;
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
            .append("name", getName())
            .append("logo", getLogo())
            .append("companyMail", getCompanyMail())
            .append("telPhone", getTelPhone())
            .append("serviceMail", getServiceMail())
            .append("remind", getRemind())
            .append("workingHours", getWorkingHours())
            .append("shopQun", getShopQun())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
