package com.manyun.admin.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.manyun.common.core.annotation.Excel;
import com.manyun.common.core.web.domain.BaseEntity;

/**
 * 协议相关对象 cnt_agreement
 *
 * @author yanwei
 * @date 2022-07-19
 */
@ApiModel("协议相关对象")
@TableName("cnt_agreement")
public class CntAgreement implements Serializable
{

    private static final long serialVersionUID = 1L;

    /** 主键 */
    @ApiModelProperty("主键")
    private String id;

    /** 协议类型;1=用户协议,2=隐私协议,3=关于我们 */
    @ApiModelProperty("协议类型;1=用户协议,2=隐私协议,3=关于我们")
    private Long agreementType;

    /** 协议标题 */
    @ApiModelProperty("协议标题")
    private String agreementTitle;

    /** 协议内容_富文本 */
    @ApiModelProperty("协议内容_富文本")
    private String agreementInfo;

    /** 创建人 */
    @ApiModelProperty("创建人")
    private String createdBy;

    /** 创建时间 */
    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    /** 更新人 */
    @ApiModelProperty("更新人")
    private String updatedBy;

    /** 更新时间 */
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
    public void setAgreementType(Long agreementType)
    {
        this.agreementType = agreementType;
    }

    public Long getAgreementType()
    {
        return agreementType;
    }
    public void setAgreementTitle(String agreementTitle)
    {
        this.agreementTitle = agreementTitle;
    }

    public String getAgreementTitle()
    {
        return agreementTitle;
    }
    public void setAgreementInfo(String agreementInfo)
    {
        this.agreementInfo = agreementInfo;
    }

    public String getAgreementInfo()
    {
        return agreementInfo;
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
            .append("agreementType", getAgreementType())
            .append("agreementTitle", getAgreementTitle())
            .append("agreementInfo", getAgreementInfo())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
