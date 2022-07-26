package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntOrderConfiguration;

/**
 * 订单配置Mapper接口
 *
 * @author yanwei
 * @date 2022-07-19
 */
public interface CntOrderConfigurationMapper extends BaseMapper<CntOrderConfiguration>
{

    /**
     * 查询订单配置列表
     *
     * @param cntOrderConfiguration 订单配置
     * @return 订单配置集合
     */
    public List<CntOrderConfiguration> selectCntOrderConfigurationList(CntOrderConfiguration cntOrderConfiguration);

}
