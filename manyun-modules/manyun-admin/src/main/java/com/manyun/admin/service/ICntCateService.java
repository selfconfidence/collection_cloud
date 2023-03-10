package com.manyun.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntCate;
import com.manyun.admin.domain.query.CateQuery;
import com.manyun.admin.domain.vo.CntCateVo;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 藏品系列_分类Service接口
 *
 * @author yanwei
 * @date 2022-07-13
 */
public interface ICntCateService extends IService<CntCate>
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
     * @param cateQuery
     * @return 藏品系列_分类集合
     */
    public TableDataInfo<CntCateVo> selectCntCateList(CateQuery cateQuery);

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
     * 批量删除藏品系列_分类
     *
     * @param ids 需要删除的藏品系列_分类主键集合
     * @return 结果
     */
    public R deleteCntCateByIds(String[] ids);
}
