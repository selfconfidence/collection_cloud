package com.manyun.admin.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.manyun.admin.domain.query.CollectionInfoQuery;
import com.manyun.admin.domain.vo.CnfCreationdVo;
import com.manyun.admin.domain.vo.CntCollectionInfoVo;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntCollectionInfoMapper;
import com.manyun.admin.domain.CntCollectionInfo;
import com.manyun.admin.service.ICntCollectionInfoService;

/**
 * 藏品详情Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-14
 */
@Service
public class CntCollectionInfoServiceImpl implements ICntCollectionInfoService
{
    @Autowired
    private CntCollectionInfoMapper cntCollectionInfoMapper;

    /**
     * 查询藏品详情
     *
     * @param id 藏品详情主键
     * @return 藏品详情
     */
    @Override
    public CntCollectionInfo selectCntCollectionInfoById(String id)
    {
        return cntCollectionInfoMapper.selectCntCollectionInfoById(id);
    }

    /**
     * 查询藏品详情列表
     *
     * @param collectionInfoQuery
     * @return 藏品详情
     */
    @Override
    public List<CntCollectionInfoVo> selectCntCollectionInfoList(CollectionInfoQuery collectionInfoQuery)
    {
        return cntCollectionInfoMapper.selectCollectionRelatedInfoList(collectionInfoQuery);
    }

    /**
     * 新增藏品详情
     *
     * @param cntCollectionInfo 藏品详情
     * @return 结果
     */
    @Override
    public int insertCntCollectionInfo(CntCollectionInfo cntCollectionInfo)
    {
        cntCollectionInfo.setId(IdUtils.getSnowflakeNextIdStr());
        cntCollectionInfo.setCreatedBy(SecurityUtils.getUsername());
        cntCollectionInfo.setCreatedTime(DateUtils.getNowDate());
        return cntCollectionInfoMapper.insertCntCollectionInfo(cntCollectionInfo);
    }

    /**
     * 修改藏品详情
     *
     * @param cntCollectionInfo 藏品详情
     * @return 结果
     */
    @Override
    public int updateCntCollectionInfo(CntCollectionInfo cntCollectionInfo)
    {
        cntCollectionInfo.setUpdatedBy(SecurityUtils.getUsername());
        cntCollectionInfo.setUpdatedTime(DateUtils.getNowDate());
        return cntCollectionInfoMapper.updateCntCollectionInfo(cntCollectionInfo);
    }

    /**
     * 批量删除藏品详情
     *
     * @param ids 需要删除的藏品详情主键
     * @return 结果
     */
    @Override
    public int deleteCntCollectionInfoByIds(String[] ids)
    {
        return cntCollectionInfoMapper.deleteCntCollectionInfoByIds(ids);
    }

    /**
     * 删除藏品详情信息
     *
     * @param id 藏品详情主键
     * @return 结果
     */
    @Override
    public int deleteCntCollectionInfoById(String id)
    {
        return cntCollectionInfoMapper.deleteCntCollectionInfoById(id);
    }
}
