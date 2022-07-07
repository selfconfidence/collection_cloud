package com.manyun.business.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 藏品详情表
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@ApiModel(value = "商品详细信息返回视图", description = "藏品详情表")
@Data
@ToString
public class CollectionInfoVo implements Serializable {

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


}
