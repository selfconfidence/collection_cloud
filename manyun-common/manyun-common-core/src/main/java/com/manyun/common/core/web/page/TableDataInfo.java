package com.manyun.common.core.web.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 * 
 * @author ruoyi
 */
@Data
public class TableDataInfo<T> implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 总记录数 */
    @ApiModelProperty("总记录数")
    private long total;

    /** 列表数据 */
    private List<T> rows;

    /** 消息状态码 */
    @ApiModelProperty("消息状态码")
    private int code;

    /** 消息内容 */
    @ApiModelProperty("消息内容")
    private String msg;

    /**
     * 表格数据对象
     */
    public TableDataInfo()
    {
    }

    /**
     * 分页
     * 
     * @param list 列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<T> list, int total)
    {
        this.rows = list;
        this.total = total;
    }
}