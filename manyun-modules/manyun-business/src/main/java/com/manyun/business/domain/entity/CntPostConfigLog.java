package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author yanwei
 * @since 2022-08-23
 */
@TableName("cnt_post_config_log")
@ApiModel(value = "CntPostConfigLog对象", description = "")
public class CntPostConfigLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("用户编号")
    private String userId;

    @ApiModelProperty("配置表(提前购配置表主键)")
    private String configId;

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

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public Integer getBuyFrequency() {
        return buyFrequency;
    }

    public void setBuyFrequency(Integer buyFrequency) {
        this.buyFrequency = buyFrequency;
    }

    @Override
    public String toString() {
        return "CntPostConfigLog{" +
        "id=" + id +
        ", userId=" + userId +
        ", configId=" + configId +
        ", buyFrequency=" + buyFrequency +
        "}";
    }
}
