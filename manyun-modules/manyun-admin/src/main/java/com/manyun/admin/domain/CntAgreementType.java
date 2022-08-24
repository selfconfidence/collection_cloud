package com.manyun.admin.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.io.Serializable;

@ApiModel("协议类型对象")
@TableName("cnt_agreement_type")
public class CntAgreementType implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("协议键值")
    private Integer agreementCode;

    @ApiModelProperty("协议标签")
    private String agreementName;

    @ApiModelProperty("状态 0:启用 1:停用")
    private Long agreementStatus;

    @ApiModelProperty("排序")
    private Long agreementSort;

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

    public Integer getAgreementCode() {
        return agreementCode;
    }

    public void setAgreementCode(Integer agreementCode) {
        this.agreementCode = agreementCode;
    }

    public void setAgreementName(String agreementName)
    {
        this.agreementName = agreementName;
    }

    public String getAgreementName()
    {
        return agreementName;
    }
    public void setAgreementStatus(Long agreementStatus)
    {
        this.agreementStatus = agreementStatus;
    }

    public Long getAgreementStatus()
    {
        return agreementStatus;
    }
    public void setAgreementSort(Long agreementSort)
    {
        this.agreementSort = agreementSort;
    }

    public Long getAgreementSort()
    {
        return agreementSort;
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
            .append("agreementCode", getAgreementCode())
            .append("agreementName", getAgreementName())
            .append("agreementStatus", getAgreementStatus())
            .append("agreementSort", getAgreementSort())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
