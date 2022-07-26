package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntSystemConfiguration;

/**
 * 系统配置Mapper接口
 *
 * @author yanwei
 * @date 2022-07-19
 */
public interface CntSystemConfigurationMapper extends BaseMapper<CntSystemConfiguration>
{

    /**
     * 查询系统配置列表
     *
     * @param cntSystemConfiguration 系统配置
     * @return 系统配置集合
     */
    public List<CntSystemConfiguration> selectCntSystemConfigurationList(CntSystemConfiguration cntSystemConfiguration);

}
