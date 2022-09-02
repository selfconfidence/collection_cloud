package com.manyun.admin.service.impl;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.CntCollectionInfo;
import com.manyun.admin.domain.query.IssuanceQuery;
import com.manyun.admin.domain.vo.CnfCreationdVo;
import com.manyun.admin.domain.vo.CnfIssuanceVo;
import com.manyun.admin.service.ICntCollectionInfoService;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.admin.mapper.CnfIssuanceMapper;
import com.manyun.admin.domain.CnfIssuance;
import com.manyun.admin.service.ICnfIssuanceService;

/**
 * 发行方Service业务层处理
 *
 * @author yanwei
 * @date 2022-08-09
 */
@Service
public class CnfIssuanceServiceImpl extends ServiceImpl<CnfIssuanceMapper,CnfIssuance> implements ICnfIssuanceService
{
    @Autowired
    private CnfIssuanceMapper cnfIssuanceMapper;

    @Autowired
    private ICntCollectionInfoService cntCollectionInfoService;

    /**
     * 查询发行方详情
     *
     * @param id 发行方主键
     * @return 发行方
     */
    @Override
    public CnfIssuance selectCnfIssuanceById(String id)
    {
        return getById(id);
    }

    /**
     * 查询发行方列表
     *
     * @param issuanceQuery
     * @return 发行方
     */
    @Override
    public TableDataInfo<CnfIssuanceVo> selectCnfIssuanceList(IssuanceQuery issuanceQuery)
    {
        PageHelper.startPage(issuanceQuery.getPageNum(),issuanceQuery.getPageSize());
        List<CnfIssuance> cnfIssuances = cnfIssuanceMapper.selectCnfIssuanceList(issuanceQuery);
        return TableDataInfoUtil.pageTableDataInfo(cnfIssuances.parallelStream().map(m ->{
            CnfIssuanceVo cnfIssuanceVo=new CnfIssuanceVo();
            BeanUtil.copyProperties(m,cnfIssuanceVo);
            return cnfIssuanceVo;
        }).collect(Collectors.toList()),cnfIssuances);
    }

    /**
     * 新增发行方
     *
     * @param cnfIssuance 发行方
     * @return 结果
     */
    @Override
    public int insertCnfIssuance(CnfIssuance cnfIssuance)
    {
        cnfIssuance.setId(IdUtils.getSnowflakeNextIdStr());
        cnfIssuance.setCreatedBy(SecurityUtils.getUsername());
        cnfIssuance.setCreatedTime(DateUtils.getNowDate());
        return save(cnfIssuance)==true?1:0;
    }

    /**
     * 修改发行方
     *
     * @param cnfIssuance 发行方
     * @return 结果
     */
    @Override
    public int updateCnfIssuance(CnfIssuance cnfIssuance)
    {
        cnfIssuance.setUpdatedBy(SecurityUtils.getUsername());
        cnfIssuance.setUpdatedTime(DateUtils.getNowDate());
        List<CntCollectionInfo> list = cntCollectionInfoService.list(Wrappers.<CntCollectionInfo>lambdaQuery().eq(CntCollectionInfo::getPublishId, cnfIssuance.getId()))
                .parallelStream().map(item -> {
                    item.setPublishAuther(cnfIssuance.getPublishAuther());
                    item.setPublishOther(cnfIssuance.getPublishOther());
                    item.setPublishInfo(cnfIssuance.getPublishInfo());
                    return item;
                }).collect(Collectors.toList());
        cntCollectionInfoService.updateBatchById(list);
        return updateById(cnfIssuance)==true?1:0;
    }

    /**
     * 批量删除发行方
     *
     * @param ids 需要删除的发行方主键
     * @return 结果
     */
    @Override
    public int deleteCnfIssuanceByIds(String[] ids)
    {
        return removeByIds(Arrays.asList(ids))==true?1:0;
    }

}
