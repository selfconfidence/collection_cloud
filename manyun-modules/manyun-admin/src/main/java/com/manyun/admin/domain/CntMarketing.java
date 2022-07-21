package com.manyun.admin.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.manyun.common.core.annotation.Excel;
import com.manyun.common.core.web.domain.BaseEntity;

@ApiModel("营销配置对象")
public class CntMarketing extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("邀请海报")
    private String invitationPoster;

    @ApiModelProperty("H5活动页面背景")
    private String posterH5;

    @ApiModelProperty("分享文案")
    private String posterText;

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
    public void setInvitationPoster(String invitationPoster)
    {
        this.invitationPoster = invitationPoster;
    }

    public String getInvitationPoster()
    {
        return invitationPoster;
    }
    public void setPosterH5(String posterH5)
    {
        this.posterH5 = posterH5;
    }

    public String getPosterH5()
    {
        return posterH5;
    }
    public void setPosterText(String posterText)
    {
        this.posterText = posterText;
    }

    public String getPosterText()
    {
        return posterText;
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
            .append("invitationPoster", getInvitationPoster())
            .append("posterH5", getPosterH5())
            .append("posterText", getPosterText())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
