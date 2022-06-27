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
 * 提前购表格
 * </p>
 *
 * @author yanwei
 * @since 2022-06-27
 */
@TableName("cnt_post_excel")
@ApiModel(value = "CntPostExcel对象", description = "提前购表格")
@Data
@ToString
public class CntPostExcel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("用户编号-内部字段")
    private String userId;

    @ApiModelProperty("业务编号;（盲盒 & 藏品）")
    private String buiId;

    @ApiModelProperty("用户;用户手机号")
    private String phone;

    @ApiModelProperty("业务名称;（盲盒 & 藏品）名称")
    private String buiName;

    @ApiModelProperty("标识;（盲盒 & 藏品）  词条")
    private String typeName;

    @ApiModelProperty("备注")
    private String reMark;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedTime;

}
