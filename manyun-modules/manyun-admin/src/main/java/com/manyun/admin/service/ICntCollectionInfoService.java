package com.manyun.admin.service;

import java.util.List;
import com.manyun.admin.domain.CntCollectionInfo;
import com.manyun.admin.domain.vo.CntCollectionInfoVo;

/**
 * 藏品详情Service接口
 *
 * @author yanwei
 * @date 2022-07-14
 */
public interface ICntCollectionInfoService
{
    /**
     * 查询藏品详情
     *
     * @param id 藏品详情主键
     * @return 藏品详情
     */
    public CntCollectionInfo selectCntCollectionInfoById(String id);

    /**
     * 查询藏品详情列表
     *
     * @param cntCollectionInfo 藏品详情
     * @return 藏品详情集合
     */
    public List<CntCollectionInfoVo> selectCntCollectionInfoList(CntCollectionInfo cntCollectionInfo);

    /**
     * 新增藏品详情
     *
     * @param cntCollectionInfo 藏品详情
     * @return 结果
     */
    public int insertCntCollectionInfo(CntCollectionInfo cntCollectionInfo);

    /**
     * 修改藏品详情
     *
     * @param cntCollectionInfo 藏品详情
     * @return 结果
     */
    public int updateCntCollectionInfo(CntCollectionInfo cntCollectionInfo);

    /**
     * 批量删除藏品详情
     *
     * @param ids 需要删除的藏品详情主键集合
     * @return 结果
     */
    public int deleteCntCollectionInfoByIds(String[] ids);

    /**
     * 删除藏品详情信息
     *
     * @param id 藏品详情主键
     * @return 结果
     */
    public int deleteCntCollectionInfoById(String id);
}
