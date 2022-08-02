package com.manyun.admin.service.impl;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntSystemConfigurationMapper;
import com.manyun.admin.domain.CntSystemConfiguration;
import com.manyun.admin.service.ICntSystemConfigurationService;

/**
 * 系统配置Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-19
 */
@Service
public class CntSystemConfigurationServiceImpl extends ServiceImpl<CntSystemConfigurationMapper,CntSystemConfiguration> implements ICntSystemConfigurationService
{
    @Autowired
    private CntSystemConfigurationMapper cntSystemConfigurationMapper;

    /**
     * 查询系统配置
     *
     * @param id 系统配置主键
     * @return 系统配置
     */
    @Override
    public CntSystemConfiguration selectCntSystemConfigurationById(String id)
    {
        return getById(id);
    }

    /**
     * 查询系统配置列表
     *
     * @param pageQuery 系统配置
     * @return 系统配置
     */
    @Override
    public TableDataInfo<CntSystemConfiguration> selectCntSystemConfigurationList(PageQuery pageQuery)
    {
        PageHelper.startPage(pageQuery.getPageNum(),pageQuery.getPageSize());
        List<CntSystemConfiguration> cntSystemConfigurations = cntSystemConfigurationMapper.selectCntSystemConfigurationList(new CntSystemConfiguration());
        return TableDataInfoUtil.pageTableDataInfo(cntSystemConfigurations,cntSystemConfigurations);
    }

    /**
     * 新增系统配置
     *
     * @param cntSystemConfiguration 系统配置
     * @return 结果
     */
    @Override
    public int insertCntSystemConfiguration(CntSystemConfiguration cntSystemConfiguration)
    {
        cntSystemConfiguration.setId(IdUtils.getSnowflakeNextIdStr());
        cntSystemConfiguration.setCreatedBy(SecurityUtils.getUsername());
        cntSystemConfiguration.setCreatedTime(DateUtils.getNowDate());
        return save(cntSystemConfiguration)==true?1:0;
    }

    /**
     * 修改系统配置
     *
     * @param cntSystemConfiguration 系统配置
     * @return 结果
     */
    @Override
    public int updateCntSystemConfiguration(CntSystemConfiguration cntSystemConfiguration)
    {
        cntSystemConfiguration.setUpdatedBy(SecurityUtils.getUsername());
        cntSystemConfiguration.setUpdatedTime(DateUtils.getNowDate());
        return updateById(cntSystemConfiguration)==true?1:0;
    }

    /**
     * 批量删除系统配置
     *
     * @param ids 需要删除的系统配置主键
     * @return 结果
     */
    @Override
    public int deleteCntSystemConfigurationByIds(String[] ids)
    {
        return removeByIds(Arrays.asList(ids))==true?1:0;
    }

    /**
     * 删除系统配置信息
     *
     * @param id 系统配置主键
     * @return 结果
     */
    @Override
    public int deleteCntSystemConfigurationById(String id)
    {
        return removeById(id)==true?1:0;
    }
}
