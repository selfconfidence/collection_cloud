package com.manyun.admin.mapper;

import java.util.List;
import com.manyun.admin.domain.CntCate;
import com.manyun.admin.domain.query.CateQuery;

/**
 * 藏品系列_分类Mapper接口
 *
 * @author yanwei
 * @date 2022-07-13
 */
public interface CntCateMapper
{
    /**
     * 查询藏品系列_分类
     *
     * @param id 藏品系列_分类主键
     * @return 藏品系列_分类
     */
    public CntCate selectCntCateById(String id);

    /**
     * 查询藏品系列_分类列表
     *
     * @param cntCate
     * @return 藏品系列_分类集合
     */
    public List<CntCate> selectCntCateList(CntCate cntCate);

    /**
     * 根据条件查询藏品系列_分类列表
     *
     * @param cateQuery
     * @return 藏品系列_分类集合
     */
    List<CntCate> selectSearchCateList(CateQuery cateQuery);

    /**
     * 新增藏品系列_分类
     *
     * @param cntCate 藏品系列_分类
     * @return 结果
     */
    public int insertCntCate(CntCate cntCate);

    /**
     * 修改藏品系列_分类
     *
     * @param cntCate 藏品系列_分类
     * @return 结果
     */
    public int updateCntCate(CntCate cntCate);

    /**
     * 删除藏品系列_分类
     *
     * @param id 藏品系列_分类主键
     * @return 结果
     */
    public int deleteCntCateById(String id);

    /**
     * 批量删除藏品系列_分类
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCntCateByIds(String[] ids);


}
