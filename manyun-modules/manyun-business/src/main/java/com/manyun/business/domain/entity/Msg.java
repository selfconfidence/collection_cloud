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
 * 用户消息
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@TableName("cnt_msg")
@ApiModel(value = "Msg对象", description = "用户消息")
@Data
@ToString
public class Msg implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("用户编号")
    private String userId;

    @ApiModelProperty("消息标题")
    private String msgTitle;

    @ApiModelProperty("消息短语")
    private String msgForm;

    @ApiModelProperty("消息类型  1=可对外，2=不可对外")
    private Integer msgType;



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
