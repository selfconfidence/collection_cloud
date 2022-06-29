package com.manyun.business.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 版本表
 * </p>
 *
 * @author yanwei
 * @since 2022-06-21
 */
@ApiModel(value = "版本返回视图", description = "版本视图")
@Data
@ToString
public class VersionVo implements Serializable {

    private static final long serialVersionUID = 1L;



    @ApiModelProperty("下载地址")
    private String packageUrl;



    @ApiModelProperty("版本号")
    private String versionCode;

    @ApiModelProperty("是否强制更新;（1=强制更新，2=非强制更新）")
    private Integer isState;

    @ApiModelProperty("更新说明")
    private String infos;


    @ApiModelProperty("发布时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

}
