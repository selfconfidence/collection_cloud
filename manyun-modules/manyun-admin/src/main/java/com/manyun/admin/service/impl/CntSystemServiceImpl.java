package com.manyun.admin.service.impl;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.query.SystemQuery;
import com.manyun.admin.domain.vo.CntSystemVo;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.admin.mapper.CntSystemMapper;
import com.manyun.admin.domain.CntSystem;
import com.manyun.admin.service.ICntSystemService;

/**
 * 平台规则Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-28
 */
@Service
public class CntSystemServiceImpl extends ServiceImpl<CntSystemMapper,CntSystem> implements ICntSystemService
{
    @Autowired
    private CntSystemMapper cntSystemMapper;

    /**
     * 查询平台规则详情
     *
     * @param id 平台规则主键
     * @return 平台规则
     */
    @Override
    public CntSystemVo selectCntSystemById(String id)
    {
        CntSystemVo cntSystemVo= Builder.of(CntSystemVo::new).build();
        CntSystem cntSystem = getById(id);
        BeanUtil.copyProperties(cntSystem,cntSystemVo);
        return cntSystemVo;
    }

    /**
     * 查询平台规则列表
     *
     * @param systemQuery
     * @return 平台规则
     */
    @Override
    public TableDataInfo<CntSystemVo> selectCntSystemList(SystemQuery systemQuery)
    {
        PageHelper.startPage(systemQuery.getPageNum(),systemQuery.getPageSize());
        List<CntSystem> cntSystems = cntSystemMapper.selectCntSystemList(systemQuery);
        return  TableDataInfoUtil.pageTableDataInfo(cntSystems.parallelStream().map(m->{
            CntSystemVo cntSystemVo=new CntSystemVo();
            BeanUtil.copyProperties(m,cntSystemVo);
            return cntSystemVo;
        }).collect(Collectors.toList()),cntSystems);
    }

    /**
     * 修改平台规则
     *
     * @param cntSystemVo
     * @return 结果
     */
    @Override
    public int updateCntSystem(CntSystemVo cntSystemVo)
    {
        CntSystem cntSystem = Builder.of(CntSystem::new).build();
        BeanUtil.copyProperties(cntSystemVo,cntSystem);
        cntSystem.setUpdatedBy(SecurityUtils.getUsername());
        cntSystem.setUpdatedTime(DateUtils.getNowDate());
        return updateById(cntSystem)==true?1:0;
    }

}
