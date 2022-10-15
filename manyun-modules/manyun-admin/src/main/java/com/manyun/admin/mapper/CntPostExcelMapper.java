package com.manyun.admin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntPostExcel;
import com.manyun.admin.domain.query.PostExcelQuery;

/**
 * 提前购格Mapper接口
 *
 * @author yanwei
 * @date 2022-07-27
 */
public interface CntPostExcelMapper extends BaseMapper<CntPostExcel>
{
    /**
     * 查询提前购格列表
     *
     * @param postExcelQuery 提前购格
     * @return 提前购格集合
     */
    public List<CntPostExcel> selectCntPostExcelList(PostExcelQuery postExcelQuery);
}
