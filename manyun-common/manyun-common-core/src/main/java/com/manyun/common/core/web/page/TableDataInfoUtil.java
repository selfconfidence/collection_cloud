package com.manyun.common.core.web.page;

import com.github.pagehelper.PageInfo;
import com.manyun.common.core.constant.HttpStatus;

import java.util.List;

/**
 * 分页补偿工具类
 */
public class TableDataInfoUtil {


    /**
     * 此方法有效进行分页,
     * @param targetList  要给视图返回的实际数据集合
     * @param sourceList   源集合,一般来讲,是数据库刚查出来的
     * @return
     */
    public static TableDataInfo pageTableDataInfo(List<?> targetList ,List<?> sourceList){
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setRows(targetList);
        rspData.setMsg("查询成功");
        rspData.setTotal(new PageInfo(sourceList).getTotal());
        return rspData;
    }


    public static TableDataInfo pageCacheData(List<?> targetList,Number total){
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setRows(targetList);
        rspData.setMsg("查询成功");
        rspData.setTotal(total.longValue());
        return rspData;
    }
}
