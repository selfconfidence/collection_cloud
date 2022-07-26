package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntWithdraw;

/**
 * 提现配置Mapper接口
 *
 * @author yanwei
 * @date 2022-07-19
 */
public interface CntWithdrawMapper extends BaseMapper<CntWithdraw>
{

    /**
     * 查询提现配置列表
     *
     * @param cntWithdraw 提现配置
     * @return 提现配置集合
     */
    public List<CntWithdraw> selectCntWithdrawList(CntWithdraw cntWithdraw);

}
