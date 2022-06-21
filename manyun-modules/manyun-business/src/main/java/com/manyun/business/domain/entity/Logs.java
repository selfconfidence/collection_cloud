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
 * 业务收支日志记录表
 * </p>
 *
 * @author yanwei
 * @since 2022-06-21
 */
@TableName("cnt_logs")
@ApiModel(value = "Logs对象", description = "业务收支日志记录表")
@Data
@ToString
public class Logs implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("业务编号")
    private String buiId;

    @ApiModelProperty("日志类型;0收入,1支出")
    private Integer isType;

    @ApiModelProperty("业务类型")
    private String modelType;

    @ApiModelProperty("记录值")
    private String formInfo;

    @ApiModelProperty("总记录;后台可见")
    private String jsonTxt;

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
