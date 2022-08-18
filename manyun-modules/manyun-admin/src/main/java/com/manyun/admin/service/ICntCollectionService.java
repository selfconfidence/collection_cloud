package com.manyun.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntCollection;
import com.manyun.admin.domain.dto.AirdropDto;
import com.manyun.admin.domain.dto.CntCollectionAlterCombineDto;
import com.manyun.admin.domain.dto.CollectionStateDto;
import com.manyun.admin.domain.excel.BachAirdopExcel;
import com.manyun.admin.domain.query.CollectionQuery;
import com.manyun.admin.domain.vo.*;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.TableDataInfo;

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
    public TableDataInfo<CntCollectionVo> selectCntCollectionList(CollectionQuery collectionQuery);

    /**
     * 新增藏品
     *
     * @param collectionAlterCombineDto
     * @return 结果
     */
    public R insertCntCollection(CntCollectionAlterCombineDto collectionAlterCombineDto);

    /**
     * 修改藏品
     *
     * @param collectionAlterCombineDto
     * @return 结果
     */
    public R updateCntCollection(CntCollectionAlterCombineDto collectionAlterCombineDto);

    /***
     * 空投
     * @param airdropDto 空投请求参数
     * @return
     */
    R airdrop(AirdropDto airdropDto);

    /**
     * 修改状态
     * @param collectionStateDto
     * @return
     */
    int updateState(CollectionStateDto collectionStateDto);

    /***
     * 批量空投
     * @param bachAirdopExcels 批量空投请求参数
     * @return
     */
    R postExcelList(List<BachAirdopExcel> bachAirdopExcels);
}
