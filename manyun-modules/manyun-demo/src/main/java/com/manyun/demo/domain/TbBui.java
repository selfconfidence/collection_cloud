package com.manyun.demo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 测试表
 * </p>
 *
 * @author yanwei
 * @since 2022-06-14
 */
@TableName("tb_bui")
@ApiModel(value = "TbBui对象", description = "测试表")
@Data
@ToString
public class TbBui extends Model<TbBui> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Integer id;

    private String name;

}
