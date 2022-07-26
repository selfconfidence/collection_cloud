package com.manyun.admin.service.impl;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class CntAgreementServiceImpl extends ServiceImpl<CntAgreementMapper,CntAgreement> implements ICntAgreementService
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
        return getById(id);
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
        return save(cntAgreement)==true?1:0;
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
        return updateById(cntAgreement)==true?1:0;
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
        return removeByIds(Arrays.asList(ids))==true?1:0;
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
        return removeById(id)==true?1:0;
    }
}
