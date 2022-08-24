package com.manyun.admin.service.impl;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.query.AgreementTypeQuery;
import com.manyun.admin.domain.vo.CntAgreementTypeVo;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.admin.mapper.CntAgreementTypeMapper;
import com.manyun.admin.domain.CntAgreementType;
import com.manyun.admin.service.ICntAgreementTypeService;

/**
 * 协议类型Service业务层处理
 *
 * @author yanwei
 * @date 2022-08-23
 */
@Service
public class CntAgreementTypeServiceImpl extends ServiceImpl<CntAgreementTypeMapper,CntAgreementType> implements ICntAgreementTypeService
{
    @Autowired
    private CntAgreementTypeMapper cntAgreementTypeMapper;

    /**
     * 查询协议类型详情
     *
     * @param id 协议类型主键
     * @return 协议类型
     */
    @Override
    public CntAgreementType selectCntAgreementTypeById(String id)
    {
        return getById(id);
    }

    /**
     * 查询协议类型列表
     *
     * @param agreementTypeQuery 协议类型
     * @return 协议类型
     */
    @Override
    public TableDataInfo<CntAgreementTypeVo> selectCntAgreementTypeList(AgreementTypeQuery agreementTypeQuery)
    {
        PageHelper.startPage(agreementTypeQuery.getPageNum(),agreementTypeQuery.getPageSize());
        List<CntAgreementType> agreementTypes = cntAgreementTypeMapper.selectCntAgreementTypeList(agreementTypeQuery);
        return TableDataInfoUtil.pageTableDataInfo(agreementTypes.parallelStream().map(m->{
            CntAgreementTypeVo cntAgreementTypeVo=new CntAgreementTypeVo();
            BeanUtils.copyProperties(m,cntAgreementTypeVo);
            return cntAgreementTypeVo;
        }).collect(Collectors.toList()), agreementTypes);
    }

    /**
     * 新增协议类型
     *
     * @param cntAgreementType 协议类型
     * @return 结果
     */
    @Override
    public int insertCntAgreementType(CntAgreementType cntAgreementType)
    {
        cntAgreementType.setId(IdUtils.getSnowflakeNextIdStr());
        cntAgreementType.setCreatedBy(SecurityUtils.getUsername());
        cntAgreementType.setCreatedTime(DateUtils.getNowDate());
        return save(cntAgreementType)==true?1:0;
    }

    /**
     * 修改协议类型
     *
     * @param cntAgreementType 协议类型
     * @return 结果
     */
    @Override
    public int updateCntAgreementType(CntAgreementType cntAgreementType)
    {
        cntAgreementType.setUpdatedBy(SecurityUtils.getUsername());
        cntAgreementType.setUpdatedTime(DateUtils.getNowDate());
        return updateById(cntAgreementType)==true?1:0;
    }

    /**
     * 批量删除协议类型
     *
     * @param ids 需要删除的协议类型主键
     * @return 结果
     */
    @Override
    public int deleteCntAgreementTypeByIds(String[] ids)
    {
        return removeByIds(Arrays.asList(ids))==true?1:0;
    }

    /**
     * 删除协议类型信息
     *
     * @param id 协议类型主键
     * @return 结果
     */
    @Override
    public int deleteCntAgreementTypeById(String id)
    {
        return removeById(id)==true?1:0;
    }
}
