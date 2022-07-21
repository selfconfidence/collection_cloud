package com.manyun.admin.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.io.Serializable;
import java.util.Date;

@ApiModel("客服对象")
public class CntCustomerService implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("菜单ID")
    private Long menuId;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("父菜单ID")
    private Long parentId;

    @ApiModelProperty("显示顺序")
    private Integer orderNum;

    @ApiModelProperty("菜单状态（0正常 1停用）")
    private String menuStatus;

    @ApiModelProperty("文章标题")
    private String articleTitle;

    @ApiModelProperty("文章详情")
    private String articleContent;

    @ApiModelProperty("创建人")
    private String createBy;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新人")
    private String updateBy;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    public void setMenuId(Long menuId)
    {
        this.menuId = menuId;
    }

    public Long getMenuId()
    {
        return menuId;
    }
    public void setMenuName(String menuName)
    {
        this.menuName = menuName;
    }

    public String getMenuName()
    {
        return menuName;
    }
    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public Long getParentId()
    {
        return parentId;
    }
    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }

    public Integer getOrderNum()
    {
        return orderNum;
    }
    public void setMenuStatus(String menuStatus)
    {
        this.menuStatus = menuStatus;
    }

    public String getMenuStatus()
    {
        return menuStatus;
    }
    public void setArticleTitle(String articleTitle)
    {
        this.articleTitle = articleTitle;
    }

    public String getArticleTitle()
    {
        return articleTitle;
    }
    public void setArticleContent(String articleContent)
    {
        this.articleContent = articleContent;
    }

    public String getArticleContent()
    {
        return articleContent;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("menuId", getMenuId())
            .append("menuName", getMenuName())
            .append("parentId", getParentId())
            .append("orderNum", getOrderNum())
            .append("menuStatus", getMenuStatus())
            .append("articleTitle", getArticleTitle())
            .append("articleContent", getArticleContent())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
