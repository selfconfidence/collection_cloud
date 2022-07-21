package com.manyun.admin.service.impl;

import java.util.List;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntAgreementMapper;
import com.manyun.admin.domain.CntAgreement;
import com.manyun.admin.service.ICntAgreementService;

/**
 * 协议相关Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-19
 */
@Service
public class CntAgreementServiceImpl implements ICntAgreementService
{
    @Autowired
    private CntAgreementMapper cntAgreementMapper;

    /**
     * 查询协议相关
     *
     * @param id 协议相关主键
     * @return 协议相关
     */
    @Override
    public CntAgreement selectCntAgreementById(String id)
    {
        return cntAgreementMapper.selectCntAgreementById(id);
    }

    /**
     * 查询协议相关列表
     *
     * @param cntAgreement 协议相关
     * @return 协议相关
     */
    @Override
    public List<CntAgreement> selectCntAgreementList(CntAgreement cntAgreement)
    {
        return cntAgreementMapper.selectCntAgreementList(cntAgreement);
    }

    /**
     * 新增协议相关
     *
     * @param cntAgreement 协议相关
     * @return 结果
     */
    @Override
    public int insertCntAgreement(CntAgreement cntAgreement)
    {
        cntAgreement.setId(IdUtils.getSnowflakeNextIdStr());
        cntAgreement.setCreatedBy(SecurityUtils.getUsername());
        cntAgreement.setCreatedTime(DateUtils.getNowDate());
        return cntAgreementMapper.insertCntAgreement(cntAgreement);
    }

    /**
     * 修改协议相关
     *
     * @param cntAgreement 协议相关
     * @return 结果
     */
    @Override
    public int updateCntAgreement(CntAgreement cntAgreement)
    {
        cntAgreement.setUpdatedBy(SecurityUtils.getUsername());
        cntAgreement.setUpdatedTime(DateUtils.getNowDate());
        return cntAgreementMapper.updateCntAgreement(cntAgreement);
    }

    /**
     * 批量删除协议相关
     *
     * @param ids 需要删除的协议相关主键
     * @return 结果
     */
    @Override
    public int deleteCntAgreementByIds(String[] ids)
    {
        return cntAgreementMapper.deleteCntAgreementByIds(ids);
    }

    /**
     * 删除协议相关信息
     *
     * @param id 协议相关主键
     * @return 结果
     */
    @Override
    public int deleteCntAgreementById(String id)
    {
        return cntAgreementMapper.deleteCntAgreementById(id);
    }
}
