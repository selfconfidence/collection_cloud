package com.manyun.admin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntPostSell;

/**
 * 提前购配置可以购买Mapper接口
 *
 * @author yanwei
 * @date 2022-07-27
 */
public interface CntPostSellMapper extends BaseMapper<CntPostSell>
{
    /**
     * 查询提前购配置可以购买列表
     *
     * @param cntPostSell 提前购配置可以购买
     * @return 提前购配置可以购买集合
     */
    public List<CntPostSell> selectCntPostSellList(CntPostSell cntPostSell);
}
