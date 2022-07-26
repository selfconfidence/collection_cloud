package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntCate;
import com.manyun.admin.domain.query.CateQuery;

/**
 * 藏品系列_分类Mapper接口
 *
 * @author yanwei
 * @date 2022-07-13
 */
public interface CntCateMapper extends BaseMapper<CntCate>
{

    /**
     * 根据条件查询藏品系列_分类列表
     *
     * @param cateQuery
     * @return 藏品系列_分类集合
     */
    List<CntCate> selectSearchCateList(CateQuery cateQuery);

}
