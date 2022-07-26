package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntMarketing;

/**
 * 营销配置Mapper接口
 *
 * @author yanwei
 * @date 2022-07-19
 */
public interface CntMarketingMapper extends BaseMapper<CntMarketing>
{

    /**
     * 查询营销配置列表
     *
     * @param cntMarketing 营销配置
     * @return 营销配置集合
     */
    public List<CntMarketing> selectCntMarketingList(CntMarketing cntMarketing);

}
