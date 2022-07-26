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
 * 用户购买藏品中间表
 * </p>
 *
 * @author yanwei
 * @since 2022-06-21
 */
@TableName("cnt_user_collection")
@ApiModel(value = "UserCollection对象", description = "用户购买藏品中间表")
@Data
@ToString
public class UserCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("用户编号")
    private String userId;

    @ApiModelProperty("藏品id")
    private String collectionId;

    @ApiModelProperty("藏品名称;")
    private String collectionName;


    @ApiModelProperty("藏品编号;购买后")
    private String collectionNumber;

    @ApiModelProperty("藏品哈希;购买后")
    private String collectionHash;

    @ApiModelProperty("链上地址;购买后 此标识,代表业务系统藏品唯一标识,往下传递即可")
    private String linkAddr;

    @ApiModelProperty("是否上链;1=未上链,2=已上链")
    private Integer isLink;

    @ApiModelProperty("内部字段,1=存在,2=不存在(寄售回滚有用)")
    private Integer isExist;


    @ApiModelProperty("来源")
    private String sourceInfo;

    @ApiModelProperty("认证机构;购买后")
    private String realCompany;

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
