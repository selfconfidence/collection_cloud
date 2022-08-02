package com.manyun.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.CntMoney;
import com.manyun.admin.domain.dto.UpdateBalanceDto;
import com.manyun.admin.domain.query.UserMoneyQuery;
import com.manyun.admin.domain.vo.CntOrderVo;
import com.manyun.admin.domain.vo.UserCollectionVo;
import com.manyun.admin.domain.vo.UserMoneyVo;
import com.manyun.admin.service.*;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntUserMapper;
import com.manyun.admin.domain.CntUser;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-12
 */
@Service
public class CntUserServiceImpl extends ServiceImpl<CntUserMapper,CntUser> implements ICntUserService
{
    @Autowired
    private CntUserMapper cntUserMapper;

    @Autowired
    private ICntOrderService orderService;

    @Autowired
    private ICntMediaService mediaService;

    @Autowired
    private ICntUserCollectionService userCollectionService;

    @Autowired
    private ICntMoneyService moneyService;


    /**
     * 用户和钱包信息
     *
     * @param userMoneyQuery 用户和钱包信息
     * @return 结果
     */
    @Override
    public TableDataInfo<UserMoneyVo> selectUserMoneyList(UserMoneyQuery userMoneyQuery)
    {
        PageHelper.startPage(userMoneyQuery.getPageNum(),userMoneyQuery.getPageSize());
        List<UserMoneyVo> userMoneyVos = cntUserMapper.selectUserMoneyList(userMoneyQuery);
        return TableDataInfoUtil.pageTableDataInfo(userMoneyVos,userMoneyVos);
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
        return updateById(cntUser)==true?1:0;
    }

    /**
     * 我的订单
     */
    @Override
    public List<CntOrderVo> myOrderList(String userId)
    {
        return orderService.myOrderList(userId);
    }

    /**
     * 我的藏品
     */
    @Override
    public List<UserCollectionVo> myCollectionList(String userId)
    {
        return  userCollectionService.myCollectionList(userId)
                    .parallelStream()
                    .map(item ->
                    {
                        item.setMediaVos(mediaService.initMediaVos(item.getCollectionId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE)); return item;
                    }).collect(Collectors.toList());
    }

    /**
     * 修改余额
     */
    @Override
    public int updateBalance(UpdateBalanceDto balanceDto) {
        CntMoney money = Builder.of(CntMoney::new).with(CntMoney::setMoneyBalance, balanceDto.getMoneyBalance()).build();
        return moneyService.update(money,Wrappers.<CntMoney>lambdaUpdate().eq(CntMoney::getUserId,balanceDto.getUserId()))==true?1:0;
    }

}
