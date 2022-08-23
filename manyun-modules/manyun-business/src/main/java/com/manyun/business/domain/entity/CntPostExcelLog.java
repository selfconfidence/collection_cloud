package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 提前购配置表_表格限制
 * </p>
 *
 * @author yanwei
 * @since 2022-08-23
 */
@TableName("cnt_post_excel_log")
@ApiModel(value = "CntPostExcelLog对象", description = "提前购配置表_表格限制")
public class CntPostExcelLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("提前购配置表_唯一限制表")
    private String id;

    @ApiModelProperty("用户编号")
    private String userId;

    @ApiModelProperty("提前购配置表(excel表格配置表)")
    private String excelId;

    @ApiModelProperty("购买次数")
    private Integer buyFrequency;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExcelId() {
        return excelId;
    }

    public void setExcelId(String excelId) {
        this.excelId = excelId;
    }

    public Integer getBuyFrequency() {
        return buyFrequency;
    }

    public void setBuyFrequency(Integer buyFrequency) {
        this.buyFrequency = buyFrequency;
    }

    @Override
    public String toString() {
        return "CntPostExcelLog{" +
        "id=" + id +
        ", userId=" + userId +
        ", excelId=" + excelId +
        ", buyFrequency=" + buyFrequency +
        "}";
    }
}
