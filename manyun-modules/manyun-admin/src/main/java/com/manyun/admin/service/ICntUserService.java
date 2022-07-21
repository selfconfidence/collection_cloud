package com.manyun.admin.service;

import java.util.List;

import com.manyun.admin.domain.CntOrder;
import com.manyun.admin.domain.CntUser;
import com.manyun.admin.domain.query.UserMoneyQuery;
import com.manyun.admin.domain.vo.UserCollectionVo;
import com.manyun.admin.domain.vo.UserMoneyVo;

/**
 * 用户Service接口
 *
 * @author yanwei
 * @date 2022-07-12
 */
public interface ICntUserService
{

    /**
     * 用户和钱包信息
     *
     * @param userMoneyQuery 用户和钱包信息
     * @return 结果
     */
    List<UserMoneyVo> selectUserMoneyList(UserMoneyQuery userMoneyQuery);

    /**
     * 修改用户
     *
     * @param cntUser 用户
     * @return 结果
     */
    public int updateCntUser(CntUser cntUser);

    /**
     * 我的订单
     */
    List<CntOrder> myOrderList(String userId);

    /**
     * 我的藏品
     */
    List<UserCollectionVo> myCollectionList(String userId);

}
