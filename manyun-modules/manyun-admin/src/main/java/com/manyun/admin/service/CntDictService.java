package com.manyun.admin.service;

import com.manyun.admin.domain.vo.CntCollectionDictVo;
import com.manyun.admin.domain.vo.CollectionCateDictVo;
import com.manyun.admin.domain.vo.CreationdDictVo;
import com.manyun.admin.domain.vo.LableDictVo;

import java.util.List;

/**
 * 字典Service接口
 *
 * @author yanwei
 * @date 2022-07-21
 */
public interface CntDictService
{
    /***
     * 查询藏品字典
     */
    List<CntCollectionDictVo> collectionDict();

    /***
     * 查询藏品系列字典
     */
    List<CollectionCateDictVo> collectionCateDict();

    /***
     * 查询创作者字典
     */
    List<CreationdDictVo> creationdDict();

    /***
     * 查询标签字典
     */
    List<LableDictVo> lableDict();
}
