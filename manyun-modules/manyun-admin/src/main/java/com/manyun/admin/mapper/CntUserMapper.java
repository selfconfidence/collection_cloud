package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntUser;
import com.manyun.admin.domain.query.UserMoneyQuery;
import com.manyun.admin.domain.vo.UserAddStatisticsVo;
import com.manyun.admin.domain.vo.UserCollectionVo;
import com.manyun.admin.domain.vo.UserMoneyVo;

/**
 * 用户Mapper接口
 *
 * @author yanwei
 * @date 2022-07-12
 */
public interface CntUserMapper extends BaseMapper<CntUser>
{

    /**
     * 用户和钱包信息
     *
     * @param userMoneyQuery 用户和钱包信息
     * @return 结果
     */
    List<UserMoneyVo> selectUserMoneyList(UserMoneyQuery userMoneyQuery);

    /***
     * 查询近七日每日新增数
     */
    List<UserAddStatisticsVo> userAddStatistics();
}
