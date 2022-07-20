package com.manyun.admin.service;

import java.util.List;
import com.manyun.admin.domain.CntCollection;
import com.manyun.admin.domain.vo.*;

/**
 * 藏品Service接口
 *
 * @author yanwei
 * @date 2022-07-14
 */
public interface ICntCollectionService
{
    /**
     * 查询藏品
     *
     * @param id 藏品主键
     * @return 藏品
     */
    public CntCollectionDetailsVo selectCntCollectionById(String id);

    /**
     * 查询藏品列表
     *
     * @param cntCollection 藏品
     * @return 藏品集合
     */
    public List<CntCollectionVo> selectCntCollectionList(CntCollection cntCollection);

    /**
     * 新增藏品
     *
     * @param collectionAlterCombineVo 藏品
     * @return 结果
     */
    public int insertCntCollection(CntCollectionAlterCombineVo collectionAlterCombineVo);

    /**
     * 修改藏品
     *
     * @param collectionAlterCombineVo 藏品
     * @return 结果
     */
    public int updateCntCollection(CntCollectionAlterCombineVo collectionAlterCombineVo);

    /**
     * 批量删除藏品
     *
     * @param ids 需要删除的藏品主键集合
     * @return 结果
     */
    public int deleteCntCollectionByIds(String[] ids);

    /***
     *  查询藏品分类下拉框
     * @return
     */
    List<CollectionCateDictVo> collectionCateDict();

    /***
     *  查询创作者下拉框
     * @return
     */
    List<CollectionCreationdDictVo> collectionCreationdDict();

    /***
     *  查询藏品标签下拉框
     * @return
     */
    List<CollectionLableDictVo> collectionLableDict();
}
