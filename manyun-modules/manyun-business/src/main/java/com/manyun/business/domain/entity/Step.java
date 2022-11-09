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
 * 藏品盲盒流转信息
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@TableName("cnt_step")
@ApiModel(value = "Step对象", description = "藏品盲盒流转信息")
@Data
@ToString
public class Step implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("模块类型")
    private String modelType;

    @ApiModelProperty("用户编号")
    private String userId;

    @ApiModelProperty("业务编号")
    private String buiId;

    @ApiModelProperty("备用字段")
    private String reMark;

    @ApiModelProperty("流转词条")
    private String formInfo;


    @ApiModelProperty("流转哈希_固定不变")
    private String formHash;


    @ApiModelProperty("流转记录溯源")
    private String formTranHash;

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
        this.createdTime = LocalDateTime.now().plusSeconds(1);
        if (this.createdTime != null)
            this.updatedTime = this.createdTime;
        this.updatedBy = this.createdBy;
    }

    public void updateD(String updateId){
        this.updatedBy = updateId;
        this.updatedTime = LocalDateTime.now();
    }
}
