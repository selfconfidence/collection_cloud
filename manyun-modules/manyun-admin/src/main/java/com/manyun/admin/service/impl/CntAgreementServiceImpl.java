package com.manyun.admin.service.impl;

import java.nio.file.Watchable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.vo.CntAgreementVo;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
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
     * @param pageQuery
     * @return 协议相关
     */
    @Override
    public TableDataInfo<CntAgreementVo> selectCntAgreementList(PageQuery pageQuery)
    {
        PageHelper.startPage(pageQuery.getPageNum(),pageQuery.getPageSize());
        List<CntAgreementVo> cntAgreementVos = cntAgreementMapper.selectCntAgreementList(new CntAgreement());
        return TableDataInfoUtil.pageTableDataInfo(cntAgreementVos,cntAgreementVos);
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
        CntAgreement agreement = getOne(Wrappers.<CntAgreement>lambdaQuery().eq(CntAgreement::getAgreementType, cntAgreement.getAgreementType()));
        Assert.isFalse(Objects.nonNull(agreement),"该协议类型已存在,请在现有数据中维护!");
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
        CntAgreement agreement = getOne(Wrappers.<CntAgreement>lambdaQuery().eq(CntAgreement::getAgreementType, cntAgreement.getAgreementType()).ne(CntAgreement::getId,cntAgreement.getId()));
        Assert.isFalse(Objects.nonNull(agreement),"该协议类型已存在,请在现有数据中维护!");
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

    /**
     * 根据类型查询协议详情
     * @param agreementType
     * @return
     */
    @Override
    public CntAgreement getInfoByType(String agreementType) {
        return getOne(Wrappers.<CntAgreement>lambdaQuery().eq(CntAgreement::getAgreementType,agreementType));
    }

}
