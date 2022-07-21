package com.manyun.admin.service.impl;

import com.manyun.admin.domain.CntOrder;
import com.manyun.admin.domain.query.UserMoneyQuery;
import com.manyun.admin.domain.vo.UserCollectionVo;
import com.manyun.admin.domain.vo.UserMoneyVo;
import com.manyun.admin.mapper.CntMediaMapper;
import com.manyun.admin.mapper.CntOrderMapper;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntUserMapper;
import com.manyun.admin.domain.CntUser;
import com.manyun.admin.service.ICntUserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-12
 */
@Service
public class CntUserServiceImpl implements ICntUserService
{
    @Autowired
    private CntUserMapper cntUserMapper;

    @Autowired
    private CntOrderMapper cntOrderMapper;

    @Autowired
    private CntMediaMapper cntMediaMapper;


    /**
     * 用户和钱包信息
     *
     * @param userMoneyQuery 用户和钱包信息
     * @return 结果
     */
    @Override
    public List<UserMoneyVo> selectUserMoneyList(UserMoneyQuery userMoneyQuery)
    {
        return cntUserMapper.selectUserMoneyList(userMoneyQuery);
    }

    /**
     * 修改用户
     *
     * @param cntUser 用户
     * @return 结果
     */
    @Override
    public int updateCntUser(CntUser cntUser)
    {
        cntUser.setUpdatedBy(SecurityUtils.getUsername());
        cntUser.setUpdatedTime(DateUtils.getNowDate());
        return cntUserMapper.updateCntUser(cntUser);
    }

    /**
     * 我的订单
     */
    @Override
    public List<CntOrder> myOrderList(String userId) {
        CntOrder cntOrder = new CntOrder();
        cntOrder.setUserId(userId);
        return cntOrderMapper.selectCntOrderList(cntOrder);
    }

    /**
     * 我的藏品
     */
    @Override
    public List<UserCollectionVo> myCollectionList(String userId) {
        List<UserCollectionVo> userCollectionVos=cntUserMapper.myCollectionList(userId);
        return  userCollectionVos
                    .parallelStream()
                    .map(item ->
                    {
                        item.setMediaVos(cntMediaMapper.initMediaVos(item.getCollectionId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE)); return item;
                    }).collect(Collectors.toList());

    }

}
