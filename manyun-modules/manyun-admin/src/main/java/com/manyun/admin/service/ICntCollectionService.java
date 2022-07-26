package com.manyun.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntCollection;
import com.manyun.admin.domain.dto.AirdropDto;
import com.manyun.admin.domain.dto.CntCollectionAlterCombineDto;
import com.manyun.admin.domain.query.CollectionQuery;
import com.manyun.admin.domain.vo.*;

/**
 * 藏品Service接口
 *
 * @author yanwei
 * @date 2022-07-14
 */
public interface ICntCollectionService extends IService<CntCollection>
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
     * @param collectionQuery
     * @return 藏品集合
     */
    public List<CntCollectionVo> selectCntCollectionList(CollectionQuery collectionQuery);

    /**
     * 新增藏品
     *
     * @param collectionAlterCombineDto
     * @return 结果
     */
    public int insertCntCollection(CntCollectionAlterCombineDto collectionAlterCombineDto);

    /**
     * 修改藏品
     *
     * @param collectionAlterCombineDto
     * @return 结果
     */
    public int updateCntCollection(CntCollectionAlterCombineDto collectionAlterCombineDto);

    /**
     * 批量删除藏品
     *
     * @param ids 需要删除的藏品主键集合
     * @return 结果
     */
    public int deleteCntCollectionByIds(String[] ids);

    /***
     * 空投
     * @param airdropDto 空投请求参数
     * @return
     */
    int airdrop(AirdropDto airdropDto);
}
