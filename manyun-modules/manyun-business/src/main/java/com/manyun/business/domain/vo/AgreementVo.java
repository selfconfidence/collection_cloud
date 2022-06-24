package com.manyun.business.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 协议相关
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@ApiModel(value = "相关协议返回视图", description = "协议相关")
@Data
public class AgreementVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("协议标题")
    private String agreementTitle;

    @ApiModelProperty("协议内容_富文本")
    private String agreementInfo;

}
