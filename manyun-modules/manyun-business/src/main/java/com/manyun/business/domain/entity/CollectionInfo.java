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
 * 藏品详情表
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@TableName("cnt_collection_info")
@ApiModel(value = "CollectionInfo对象", description = "藏品详情表")
@Data
@ToString
public class CollectionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("藏品编号")
    private String collectionId;

    @ApiModelProperty("藏品故事")
    private String lookInfo;

    @ApiModelProperty("购买须知-富文本")
    private String customerTail;

    @ApiModelProperty("发行方")
    private String publishOther;


    @ApiModelProperty("发行方头像")
    private String publishAuther;

    @ApiModelProperty("发行方简介")
    private String publishInfo;


    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedTime;

}
