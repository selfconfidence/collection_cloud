package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 版本表
 * </p>
 *
 * @author yanwei
 * @since 2022-06-21
 */
@TableName("cnt_version")
@ApiModel(value = "Version对象", description = "版本表")
@Data
@ToString
public class Version implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("下载地址")
    private String packageUrl;

    @ApiModelProperty("类型;（安卓 1 ,ios 2）")
    private Integer isType;

    @ApiModelProperty("版本号")
    private String versionCode;

    @ApiModelProperty("是否强制更新;（1=强制更新，2=非强制更新）")
    private Integer isState;

    @ApiModelProperty("更新说明")
    private String infos;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedTime;

    public void createD(String createId){
        this.createdBy = createId;
        this.createdTime = LocalDateTime.now();
        if (this.createdTime != null)
            this.updatedTime = this.createdTime;
        this.updatedBy = this.createdBy;
    }

    public void updateD(String updateId){
        this.updatedBy = updateId;
        this.updatedTime = LocalDateTime.now();
    }
}
