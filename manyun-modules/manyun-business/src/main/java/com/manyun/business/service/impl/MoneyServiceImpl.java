package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.manyun.business.domain.dto.LogInfoDto;
import com.manyun.business.domain.dto.UserMoneyDto;
import com.manyun.business.domain.entity.*;
import com.manyun.business.domain.form.AccountInfoForm;
import com.manyun.business.domain.query.CheckOrderPayQuery;
import com.manyun.business.domain.query.MoneyLogQuery;
import com.manyun.business.domain.vo.AccountInfoVo;
import com.manyun.business.domain.vo.CheckOrderVo;
import com.manyun.business.domain.vo.MoneyLogVo;
import com.manyun.business.mapper.MoneyMapper;
import com.manyun.business.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.comm.api.domain.form.UserRealMoneyForm;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.enums.AuctionStatus;
import com.manyun.common.core.enums.OrderStatus;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.manyun.common.core.constant.BusinessConstants.LogsTypeConstant.POLL_SOURCE;
import static com.manyun.common.core.constant.BusinessConstants.LogsTypeConstant.PULL_SOURCE;
import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.MONEY_TYPE;

/**
 * <p>
 * 钱包表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
public class MoneyServiceImpl extends ServiceImpl<MoneyMapper, Money> implements IMoneyService {

    @Autowired
    private ILogsService logsService;

    @Autowired
    private MoneyMapper moneyMapper;

    @Autowired
    @Lazy
    private IAuctionMarginService auctionMarginService;

    @Autowired
    @Lazy
    private IOrderService orderService;

    @Autowired
    @Lazy
    private IAuctionOrderService auctionOrderService;


    /**
     * 余额增加
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderBack(String userId,BigDecimal moneyBln,String formInfo){
        Money money = getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId, userId));
        money.setMoneyBalance(money.getMoneyBalance().add(moneyBln));
        money.updateD(userId);
        logsService.saveLogs(LogInfoDto.builder().buiId(userId).jsonTxt(formInfo).formInfo( moneyBln.toString()).isType(PULL_SOURCE).modelType(MONEY_TYPE).build());
        updateById(money);
    }

    /**
     * 加余额
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMoney(String userId,BigDecimal money,String formInfo){
        Money moneyEntiy = getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId, userId));
        moneyEntiy.setMoneyBalance(moneyEntiy.getMoneyBalance().add(money));
        logsService.saveLogs(LogInfoDto.builder().buiId(userId).jsonTxt(formInfo).formInfo( money.toString()).isType(PULL_SOURCE).modelType(MONEY_TYPE).build());
        updateById(moneyEntiy);

    }


    /**
     * 根据进行进行扣除余额操作 —— 组合支付使用
     * @param userId
     * @param realPayMoney
     * @param formInfo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BigDecimal ordePay(String outHost, String userId, BigDecimal realPayMoney, String formInfo) {
        // 剩余金额
        BigDecimal moneyPayMoney = NumberUtil.add(0D);
        Money money = getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId, userId));
        //1. 当前钱包金额是否满足当前扣得金额?
        // 够 - 直接扣除,记录日志
        // 不够，钱包余额扣完，剩余得返回 交给 第三方支付
        //Assert.isTrue(money.getMoneyBalance().compareTo(realPayMoney)>=0,"余额不足,请核实!");
        if (money.getMoneyBalance().compareTo(realPayMoney)>=0){
            // 够扣
            money.setMoneyBalance(NumberUtil.sub(money.getMoneyBalance(),realPayMoney));
            logsService.saveLogs(LogInfoDto.builder().buiId(userId).jsonTxt(formInfo).formInfo(realPayMoney.toString()).isType(POLL_SOURCE).modelType(MONEY_TYPE).build());
        } else  {
            // 不够扣
            moneyPayMoney = NumberUtil.sub(realPayMoney, money.getMoneyBalance());
            if (moneyPayMoney.compareTo(realPayMoney) !=0)
            logsService.saveLogs(LogInfoDto.builder().buiId(userId).jsonTxt(formInfo).formInfo( money.getMoneyBalance().toString()).isType(POLL_SOURCE).modelType(MONEY_TYPE).build());

            money.setMoneyBalance(NumberUtil.add(0D));

        }
        money.updateD(userId);
        updateById(money);
         return moneyPayMoney;
    }

    @Override
    public AccountInfoVo accountInfo(String userId) {
        Money moneyUser = getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId, userId));
        AccountInfoVo accountInfo = Builder.of(AccountInfoVo::new).build();
        BeanUtil.copyProperties(moneyUser,accountInfo);
        // 是否绑定银行卡了
        return accountInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAccountInfo(String userId, AccountInfoForm accountInfoForm) {
        Money money = getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId, userId));
        BeanUtil.copyProperties(accountInfoForm,money);
        money.updateD(userId);
        updateById(money);
    }

    /**
     * 分页查询收支明细列表
     * @param userId
     * @param moneyLogQuery
     * @return
     */
    @Override
    public TableDataInfo<MoneyLogVo> pageMoneyLog(String userId, MoneyLogQuery moneyLogQuery) {
        PageHelper.startPage(moneyLogQuery.getPageNum(),moneyLogQuery.getPageSize());
        List<Logs> logsList = logsService.list(Wrappers.<Logs>lambdaQuery().eq(Logs::getBuiId, userId).eq(Logs::getModelType, MONEY_TYPE).eq(Objects.nonNull(moneyLogQuery.getIsType()), Logs::getIsType, moneyLogQuery.getIsType()).apply(Objects.nonNull(moneyLogQuery.getCreatedTime()), " and DATE_FORMAT(created_time,'%Y-%m-%d') = " + moneyLogQuery.getCreatedTime() + " ").orderByDesc(Logs::getCreatedTime));
        List<MoneyLogVo> moneyLogVos = logsList.parallelStream().map(this::initMoneyLogVo).collect(Collectors.toList());
        return TableDataInfoUtil.pageTableDataInfo(moneyLogVos,logsList);
    }

    @Override
    public void initUserMoney(String userId) {
        // 乐观执行
        long isExist = count(Wrappers.<Money>lambdaQuery().eq(Money::getUserId, userId));
        if (isExist >=1)
            return;
        Money money = Builder.of(Money::new).build();
        money.setUserId(userId);
        money.setId(IdUtil.getSnowflakeNextIdStr());
        money.setMoneyBalance(NumberUtil.add(0D));
        money.createD(userId);
        saveOrUpdate(money);
    }

    private MoneyLogVo initMoneyLogVo(Logs logs) {
        MoneyLogVo moneyLogVo = Builder.of(MoneyLogVo::new).build();
        BeanUtil.copyProperties(logs,moneyLogVo);
        return moneyLogVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateUserMoney(UserRealMoneyForm userRealMoneyForm) {

        Money moneyUser = getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId, userRealMoneyForm.getUserId()));
        moneyUser.setCartNo(userRealMoneyForm.getCartNo());
        moneyUser.setRealName(userRealMoneyForm.getUserRealName());
        moneyUser.setBankCart(userRealMoneyForm.getBankcard());
        moneyUser.updateD(userRealMoneyForm.getUserId());
        updateById(moneyUser);
        return R.ok();
    }

    @Override
    public R<String> checkIdentity(String identityNo) {
        Money moneyUser = getOne(Wrappers.<Money>lambdaQuery().eq(Money::getCartNo, identityNo));
        if (moneyUser == null) {
            return R.ok();
        } else {
            //return R.fail("当前身份证已用于实名，请勿重复验证");
            return R.fail(moneyUser.getUserId());
        }
    }

    /**
     * 杉德支付所需参数
     * @param userId
     * @return
     */
    @Override
    public UserMoneyDto userMoneyInfo(String userId) {
        return moneyMapper.userMoneyInfo(userId);
    }


    /**
     * 检查支付返回结果
     * @param checkOrderPayQuery
     * @return
     */
    @Override
    public CheckOrderVo checkOrderPayStatus(CheckOrderPayQuery checkOrderPayQuery) {
        CheckOrderVo checkOrderVo = new CheckOrderVo();
        BigDecimal orderAmount = BigDecimal.ZERO;
        Integer payStatus = Integer.valueOf(0);
        Integer payType = Integer.valueOf(0);
        BigDecimal moneyBln = BigDecimal.ZERO;
        LocalDateTime payTime = LocalDateTime.now();

        switch (checkOrderPayQuery.getType()) {
            case 1 :
                Order order = orderService.getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, checkOrderPayQuery.getOrderNo()));
                if (Objects.isNull(order)) order = orderService.getOne(Wrappers.<Order>lambdaQuery().eq(Order::getId, checkOrderPayQuery.getOrderNo()));
                if (order == null) {
                    break;
                }
                orderAmount = order.getOrderAmount();
                if (OrderStatus.OVER_ORDER.getCode().equals(order.getOrderStatus())) {
                    payStatus = 1;
                }
                payType = Optional.<Integer>of(order.getPayType()).orElse(Integer.valueOf(0));
                moneyBln = order.getMoneyBln();
                payTime = order.getPayTime();
                break;
            case 2 :
                AuctionMargin auctionMargin = auctionMarginService.getById(checkOrderPayQuery.getOrderNo());
                if (auctionMargin == null) {
                    break;
                }
                orderAmount = auctionMargin.getMargin();
                payStatus = auctionMargin.getPayMarginStatus();
                payType = auctionMargin.getPayType();
                moneyBln = auctionMargin.getMoneyBln();
                payTime = auctionMargin.getUpdatedTime();
                break;
            case 3 :
                AuctionOrder auctionOrder = auctionOrderService.getOne(Wrappers.<AuctionOrder>lambdaQuery().eq(AuctionOrder::getOrderNo, checkOrderPayQuery.getOrderNo()));
                if (Objects.isNull(auctionOrder)) auctionOrder = auctionOrderService.getOne(Wrappers.<AuctionOrder>lambdaQuery().eq(AuctionOrder::getSendAuctionId, checkOrderPayQuery.getOrderNo()));
                if (auctionOrder == null) {
                    break;
                }
                orderAmount = auctionOrder.getOrderAmount();
                if (AuctionStatus.PAY_SUCCESS.getCode().equals(auctionOrder.getAuctionStatus())) {
                    payStatus = 1;
                }
                payType = auctionOrder.getPayType();
                moneyBln = auctionOrder.getMoneyBln();
                payTime = auctionOrder.getPayTime();
                break;
            case 4 :
                AuctionOrder auctionPayOrder = auctionOrderService.getOne(Wrappers.<AuctionOrder>lambdaQuery().eq(AuctionOrder::getOrderNo, checkOrderPayQuery.getOrderNo()));
                if (Objects.isNull(auctionPayOrder)) auctionPayOrder = auctionOrderService.getOne(Wrappers.<AuctionOrder>lambdaQuery().eq(AuctionOrder::getSendAuctionId, checkOrderPayQuery.getOrderNo()));
                if (auctionPayOrder == null) {
                    break;
                }
                orderAmount = auctionPayOrder.getOrderAmount();
                if (AuctionStatus.PAY_SUCCESS.getCode().equals(auctionPayOrder.getAuctionStatus())) {
                    payStatus = 1;
                }
                payType = auctionPayOrder.getPayType();
                moneyBln = auctionPayOrder.getMoneyBln();
                payTime = auctionPayOrder.getPayTime();
                break;
            default:
                break;

        }
        checkOrderVo.setOrderAmount(orderAmount);
        checkOrderVo.setPayStatus(payStatus);
        checkOrderVo.setPayType(payType);
        checkOrderVo.setMoneyBln(moneyBln);
        checkOrderVo.setPayTime(payTime);

        return checkOrderVo;
    }

}
