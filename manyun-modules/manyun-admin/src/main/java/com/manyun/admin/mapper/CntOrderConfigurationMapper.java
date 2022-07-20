package com.manyun.admin.mapper;

import java.util.List;
import com.manyun.admin.domain.CntOrderConfiguration;

/**
 * 订单配置Mapper接口
 * 
 * @author yanwei
 * @date 2022-07-19
 */
public interface CntOrderConfigurationMapper 
{
    /**
     * 查询订单配置
     * 
     * @param id 订单配置主键
     * @return 订单配置
     */
    public CntOrderConfiguration selectCntOrderConfigurationById(String id);

    /**
     * 查询订单配置列表
     * 
     * @param cntOrderConfiguration 订单配置
     * @return 订单配置集合
     */
    public List<CntOrderConfiguration> selectCntOrderConfigurationList(CntOrderConfiguration cntOrderConfiguration);

    /**
     * 新增订单配置
     * 
     * @param cntOrderConfiguration 订单配置
     * @return 结果
     */
    public int insertCntOrderConfiguration(CntOrderConfiguration cntOrderConfiguration);

    /**
     * 修改订单配置
     * 
     * @param cntOrderConfiguration 订单配置
     * @return 结果
     */
    public int updateCntOrderConfiguration(CntOrderConfiguration cntOrderConfiguration);

    /**
     * 删除订单配置
     * 
     * @param id 订单配置主键
     * @return 结果
     */
    public int deleteCntOrderConfigurationById(String id);

    /**
     * 批量删除订单配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCntOrderConfigurationByIds(String[] ids);
}
