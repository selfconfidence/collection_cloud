package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 客服中心;客服中心主体表
 * </p>
 *
 * @author
 * @since 2022-06-28
 */
@TableName("cnt_customer_service")
@ApiModel(value = "客服中心对象", description = "客服中心;客服中心主体表")
@Data
public class CustomerService implements Serializable {

    @ApiModelProperty("菜单Id")
    private Integer id;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("父菜单Id")
    private Integer parentId;

    @ApiModelProperty("显示顺序")
    private Integer orderNum;

    @ApiModelProperty("菜单状态 0正常 1停用")
    private String menuStatus;

    @ApiModelProperty("文章标题")
    private String articleTitle;

    @ApiModelProperty("文章详情")
    private String articleContent;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改人")
    private String updateBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

}
