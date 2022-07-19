package com.manyun.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 藏品盲盒流转信息
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Data
@ToString
@ApiModel("资产流转信息")
public class StepVo implements Serializable {



    @ApiModelProperty("模块类型")
    private String modelType;

    @ApiModelProperty("用户编号")
    private String userId;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户头像")
    private String headImage;

    @ApiModelProperty("用户id;平台内部生成,短编号")
    private String userHostId;

    @ApiModelProperty("业务编号")
    private String buiId;

    @ApiModelProperty("备用字段")
    private String reMark;

    @ApiModelProperty("流转词条")
    private String formInfo;

    @ApiModelProperty("流转哈希")
    private String formHash;

    @ApiModelProperty("流转时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

}
