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
import com.manyun.admin.mapper.CntOrderConfigurationMapper;
import com.manyun.admin.domain.CntOrderConfiguration;
import com.manyun.admin.service.ICntOrderConfigurationService;

/**
 * 订单配置Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-19
 */
@Service
public class CntOrderConfigurationServiceImpl extends ServiceImpl<CntOrderConfigurationMapper,CntOrderConfiguration> implements ICntOrderConfigurationService
{
    @Autowired
    private CntOrderConfigurationMapper cntOrderConfigurationMapper;

    /**
     * 查询订单配置
     *
     * @param id 订单配置主键
     * @return 订单配置
     */
    @Override
    public CntOrderConfiguration selectCntOrderConfigurationById(String id)
    {
        return getById(id);
    }

    /**
     * 查询订单配置列表
     *
     * @param pageQuery
     * @return 订单配置
     */
    @Override
    public TableDataInfo<CntOrderConfiguration> selectCntOrderConfigurationList(PageQuery pageQuery)
    {
        PageHelper.startPage(pageQuery.getPageNum(),pageQuery.getPageSize());
        List<CntOrderConfiguration> cntOrderConfigurations = cntOrderConfigurationMapper.selectCntOrderConfigurationList(new CntOrderConfiguration());
        return TableDataInfoUtil.pageTableDataInfo(cntOrderConfigurations,cntOrderConfigurations);
    }

    /**
     * 新增订单配置
     *
     * @param cntOrderConfiguration 订单配置
     * @return 结果
     */
    @Override
    public int insertCntOrderConfiguration(CntOrderConfiguration cntOrderConfiguration)
    {
        cntOrderConfiguration.setId(IdUtils.getSnowflakeNextIdStr());
        cntOrderConfiguration.setCreatedBy(SecurityUtils.getUsername());
        cntOrderConfiguration.setCreatedTime(DateUtils.getNowDate());
        return save(cntOrderConfiguration)==true?1:0;
    }

    /**
     * 修改订单配置
     *
     * @param cntOrderConfiguration 订单配置
     * @return 结果
     */
    @Override
    public int updateCntOrderConfiguration(CntOrderConfiguration cntOrderConfiguration)
    {
        cntOrderConfiguration.setUpdatedBy(SecurityUtils.getUsername());
        cntOrderConfiguration.setUpdatedTime(DateUtils.getNowDate());
        return updateById(cntOrderConfiguration)==true?1:0;
    }

    /**
     * 批量删除订单配置
     *
     * @param ids 需要删除的订单配置主键
     * @return 结果
     */
    @Override
    public int deleteCntOrderConfigurationByIds(String[] ids)
    {
        return removeByIds(Arrays.asList(ids))==true?1:0;
    }

    /**
     * 删除订单配置信息
     *
     * @param id 订单配置主键
     * @return 结果
     */
    @Override
    public int deleteCntOrderConfigurationById(String id)
    {
        return removeById(id)==true?1:0;
    }
}
