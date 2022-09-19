package com.manyun.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.*;
import com.manyun.admin.domain.dto.MyBoxDto;
import com.manyun.admin.domain.dto.MyCollectionDto;
import com.manyun.admin.domain.dto.MyOrderDto;
import com.manyun.admin.domain.dto.UpdateBalanceDto;
import com.manyun.admin.domain.excel.UserPhoneExcel;
import com.manyun.admin.domain.query.UserMoneyQuery;
import com.manyun.admin.domain.vo.*;
import com.manyun.admin.service.*;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.enums.CntSystemEnum;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntUserMapper;

import java.util.*;
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

    @Autowired
    private ICntUserBoxService userBoxService;

    @Autowired
    private ICntConsignmentService consignmentService;

    @Autowired
    private ICntSystemService systemService;


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
        List<CntUser> cntUsers = list();
        CntSystem system = systemService.getOne(Wrappers.<CntSystem>lambdaQuery().eq(CntSystem::getSystemType, CntSystemEnum.INVITEPEOPLE_ISBUY_GOODS));
        List<CntOrder> orderList = orderService.list(
                Wrappers
                        .<CntOrder>lambdaQuery()
                        .eq(CntOrder::getOrderStatus, 1)
                        .eq(StringUtils.isNotBlank(system.getSystemVal()),CntOrder::getBuiId,system.getSystemVal())
                        .notIn(
                                CntOrder::getId,
                                consignmentService.list(Wrappers.<CntConsignment>lambdaQuery().isNotNull(CntConsignment::getOrderId).select(CntConsignment::getOrderId))
                        )
        ).parallelStream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getUserId()))), ArrayList::new));
        return TableDataInfoUtil.pageTableDataInfo(userMoneyVos.parallelStream().map(m->{
            m.setInviteNumber(cntUsers.parallelStream().filter(ff->(m.getId().equals(ff.getParentId()))).count());
            m.setRealNumber(cntUsers.parallelStream().filter(ff->(m.getId().equals(ff.getParentId()) && ff.getIsReal()==2)).count());
            if(Objects.nonNull(system) && StringUtils.isNotBlank(system.getSystemVal())){
               List<String> userIds = cntUsers.parallelStream().filter(ff->(m.getId().equals(ff.getParentId()) && ff.getIsReal()==2)).map(CntUser::getId).collect(Collectors.toList());
               m.setInviteGoodsNumber(orderList.parallelStream().filter(ff->userIds.contains(ff.getUserId())).count());
            }else {
                m.setInviteGoodsNumber(Long.valueOf(0));
            }
            return m;
        }).collect(Collectors.toList()), userMoneyVos);
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
    public TableDataInfo<CntOrderVo> myOrderList(MyOrderDto orderDto)
    {
        PageHelper.startPage(orderDto.getPageNum(),orderDto.getPageSize());
        List<CntOrderVo> cntOrderVos = orderService.myOrderList(orderDto.getUserId());
        List<CntUserCollection> userCollections = userCollectionService.list();
        return TableDataInfoUtil.pageTableDataInfo(cntOrderVos.parallelStream().map(m->{
            m.setMediaVos(mediaService.initMediaVos(m.getBuiId(),m.getGoodsType()==0?BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE:BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));
            if(m.getGoodsType() == 0){
                Optional<CntUserCollection> optional = userCollections.parallelStream().filter(ff -> ff.getId().equals(m.getUserBuiId())).findFirst();
                if(optional.isPresent()){
                    m.setCollectionHash(optional.get().getCollectionHash());
                }
            }
            return m;
        }).collect(Collectors.toList()),cntOrderVos);
    }

    /**
     * 我的藏品
     */
    @Override
    public TableDataInfo<UserCollectionVo> myCollectionList(MyCollectionDto collectionDto)
    {
        PageHelper.startPage(collectionDto.getPageNum(),collectionDto.getPageSize());
        List<UserCollectionVo> userCollectionVos = userCollectionService.myCollectionList(collectionDto.getUserId());
        return TableDataInfoUtil.pageTableDataInfo(userCollectionVos.parallelStream().map(m->{
            m.setMediaVos(mediaService.initMediaVos(m.getCollectionId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE));
            return m;
        }).collect(Collectors.toList()),userCollectionVos);
    }

    /**
     * 我的盲盒
     * @param boxDto
     * @return
     */
    @Override
    public TableDataInfo<UserBoxVo> myBoxList(MyBoxDto boxDto) {
        PageHelper.startPage(boxDto.getPageNum(),boxDto.getPageSize());
        List<UserBoxVo> userBoxVos = userBoxService.myBoxList(boxDto.getUserId());
        return TableDataInfoUtil.pageTableDataInfo(userBoxVos.parallelStream().map(m->{
            m.setMediaVos(mediaService.initMediaVos(m.getBoxId(), BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));
            return m;
        }).collect(Collectors.toList()),userBoxVos);
    }

    /***
     * 查询近七日每日新增数
     */
    @Override
    public List<UserAddStatisticsVo> userAddStatistics() {
        return cntUserMapper.userAddStatistics();
    }

    /**
     * 修改余额
     */
    @Override
    public int updateBalance(UpdateBalanceDto balanceDto) {
        CntMoney money = Builder.of(CntMoney::new).with(CntMoney::setMoneyBalance, balanceDto.getMoneyBalance()).build();
        return moneyService.update(money,Wrappers.<CntMoney>lambdaUpdate().eq(CntMoney::getUserId,balanceDto.getUserId()))==true?1:0;
    }

    /**
     * 导出用户手机号
     * @return
     */
    @Override
    public List<UserPhoneExcel> selectUserList() {
        return list(Wrappers.<CntUser>lambdaQuery().orderByAsc(CntUser::getCreatedTime)).parallelStream().map(m->{
            UserPhoneExcel userPhoneExcel=new UserPhoneExcel();
            BeanUtils.copyProperties(m,userPhoneExcel);
            return userPhoneExcel;
        }).collect(Collectors.toList());
    }

}
