package com.manyun.admin.service.impl;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.CntPostExist;
import com.manyun.admin.domain.CntPostSell;
import com.manyun.admin.domain.query.PostConfigQuery;
import com.manyun.admin.domain.vo.CntPostConfigVo;
import com.manyun.admin.service.ICntPostExistService;
import com.manyun.admin.service.ICntPostSellService;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntPostConfigMapper;
import com.manyun.admin.domain.CntPostConfig;
import com.manyun.admin.service.ICntPostConfigService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 提前购配置-只能有一条Service业务层处理
 *
 * @author yanwei
 * @date 2022-08-15
 */
@Service
public class CntPostConfigServiceImpl extends ServiceImpl<CntPostConfigMapper,CntPostConfig> implements ICntPostConfigService
{
    @Autowired
    private CntPostConfigMapper cntPostConfigMapper;

    @Autowired
    private ICntPostExistService postExistService;

    @Autowired
    private ICntPostSellService postSellService;

    /**
     * 查询提前购配置-只能有一条详情
     *
     * @param id 提前购配置-只能有一条主键
     * @return 提前购配置-只能有一条
     */
    @Override
    public CntPostConfig selectCntPostConfigById(String id)
    {
        return getById(id);
    }

    /**
     * 查询提前购配置-只能有一条列表
     *
     * @param postConfigQuery
     * @return 提前购配置-只能有一条
     */
    @Override
    public TableDataInfo<CntPostConfigVo> selectCntPostConfigList(PostConfigQuery postConfigQuery)
    {
        PageHelper.startPage(postConfigQuery.getPageNum(),postConfigQuery.getPageSize());
        List<CntPostConfig> cntPostConfigs = cntPostConfigMapper.selectCntPostConfigList(postConfigQuery);
        return TableDataInfoUtil.pageTableDataInfo(cntPostConfigs.stream().map(m->{
            CntPostConfigVo postConfigVo=new CntPostConfigVo();
            BeanUtil.copyProperties(m,postConfigVo);
            return postConfigVo;
        }).collect(Collectors.toList()),cntPostConfigs);
    }

    /**
     * 新增提前购配置-只能有一条
     *
     * @param cntPostConfig 提前购配置-只能有一条
     * @return 结果
     */
    @Override
    public int insertCntPostConfig(CntPostConfig cntPostConfig)
    {
        cntPostConfig.setId(IdUtils.getSnowflakeNextIdStr());
        cntPostConfig.setCreatedBy(SecurityUtils.getUsername());
        cntPostConfig.setCreatedTime(DateUtils.getNowDate());
        return save(cntPostConfig)==true?1:0;
    }

    /**
     * 修改提前购配置-只能有一条
     *
     * @param cntPostConfig 提前购配置-只能有一条
     * @return 结果
     */
    @Override
    public int updateCntPostConfig(CntPostConfig cntPostConfig)
    {
        cntPostConfig.setUpdatedBy(SecurityUtils.getUsername());
        cntPostConfig.setUpdatedTime(DateUtils.getNowDate());
        return updateById(cntPostConfig)==true?1:0;
    }

    /**
     * 批量删除提前购配置-只能有一条
     *
     * @param ids 需要删除的提前购配置-只能有一条主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCntPostConfigByIds(String[] ids)
    {
        removeByIds(Arrays.asList(ids));
        postExistService.remove(Wrappers.<CntPostExist>lambdaQuery().in(CntPostExist::getConfigId,ids));
        postSellService.remove(Wrappers.<CntPostSell>lambdaQuery().in(CntPostSell::getConfigId,ids));
        return 1;
    }

}
