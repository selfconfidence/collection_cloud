package com.manyun.admin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntSystemWithdraw;
import com.manyun.admin.domain.query.SystemWithdrawQuery;

/**
 * 系统余额提现Mapper接口
 *
 * @author yanwei
 * @date 2022-09-17
 */
public interface CntSystemWithdrawMapper extends BaseMapper<CntSystemWithdraw>
{
    /**
     * 查询系统余额提现列表
     *
     * @param systemWithdrawQuery 系统余额提现
     * @return 系统余额提现集合
     */
    public List<CntSystemWithdraw> selectCntSystemWithdrawList(SystemWithdrawQuery systemWithdrawQuery);
}
