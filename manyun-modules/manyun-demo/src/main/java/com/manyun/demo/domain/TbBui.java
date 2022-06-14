package com.manyun.demo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;

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
public class TbBui implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TbBui{" +
        "id=" + id +
        ", name=" + name +
        "}";
    }
}
