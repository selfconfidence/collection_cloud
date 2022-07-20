package com.manyun.admin.mapper;

import java.util.List;
import com.manyun.admin.domain.CntSystemConfiguration;

/**
 * 系统配置Mapper接口
 * 
 * @author yanwei
 * @date 2022-07-19
 */
public interface CntSystemConfigurationMapper 
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
     * @param cntSystemConfiguration 系统配置
     * @return 系统配置集合
     */
    public List<CntSystemConfiguration> selectCntSystemConfigurationList(CntSystemConfiguration cntSystemConfiguration);

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
     * 删除系统配置
     * 
     * @param id 系统配置主键
     * @return 结果
     */
    public int deleteCntSystemConfigurationById(String id);

    /**
     * 批量删除系统配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCntSystemConfigurationByIds(String[] ids);
}
