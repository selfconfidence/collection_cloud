package com.manyun.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntSystemConfiguration;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 系统配置Service接口
 *
 * @author yanwei
 * @date 2022-07-19
 */
public interface ICntSystemConfigurationService extends IService<CntSystemConfiguration>
{
    /**
     * 查询系统配置
     *
     * @param id 系统配置主键
     * @return 系统配置
     */
    public CntSystemConfiguration selectCntSystemConfigurationById(String id);

    /**
     * 查询系统配置列表
     *
     * @param pageQuery
     * @return 系统配置集合
     */
    public TableDataInfo<CntSystemConfiguration> selectCntSystemConfigurationList(PageQuery pageQuery);

    /**
     * 新增系统配置
     *
     * @param cntSystemConfiguration 系统配置
     * @return 结果
     */
    public int insertCntSystemConfiguration(CntSystemConfiguration cntSystemConfiguration);

    /**
     * 修改系统配置
     *
     * @param cntSystemConfiguration 系统配置
     * @return 结果
     */
    public int updateCntSystemConfiguration(CntSystemConfiguration cntSystemConfiguration);

    /**
     * 批量删除系统配置
     *
     * @param ids 需要删除的系统配置主键集合
     * @return 结果
     */
    public int deleteCntSystemConfigurationByIds(String[] ids);

    /**
     * 删除系统配置信息
     *
     * @param id 系统配置主键
     * @return 结果
     */
    public int deleteCntSystemConfigurationById(String id);
}
